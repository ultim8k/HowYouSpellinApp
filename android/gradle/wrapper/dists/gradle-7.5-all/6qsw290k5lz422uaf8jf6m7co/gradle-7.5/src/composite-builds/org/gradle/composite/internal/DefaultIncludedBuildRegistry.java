/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.composite.internal;

import com.google.common.base.MoreObjects;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.component.BuildIdentifier;
import org.gradle.api.internal.BuildDefinition;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.internal.SettingsInternal;
import org.gradle.api.internal.artifacts.DefaultBuildIdentifier;
import org.gradle.initialization.buildsrc.BuildSrcDetector;
import org.gradle.internal.build.BuildAddedListener;
import org.gradle.internal.build.BuildState;
import org.gradle.internal.build.BuildStateRegistry;
import org.gradle.internal.build.IncludedBuildFactory;
import org.gradle.internal.build.IncludedBuildState;
import org.gradle.internal.build.RootBuildState;
import org.gradle.internal.build.StandAloneNestedBuild;
import org.gradle.internal.buildtree.NestedBuildTree;
import org.gradle.internal.composite.IncludedBuildInternal;
import org.gradle.internal.concurrent.CompositeStoppable;
import org.gradle.internal.concurrent.Stoppable;
import org.gradle.internal.event.ListenerManager;
import org.gradle.util.Path;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class DefaultIncludedBuildRegistry implements BuildStateRegistry, Stoppable {
    private final IncludedBuildFactory includedBuildFactory;
    private final IncludedBuildDependencySubstitutionsBuilder dependencySubstitutionsBuilder;
    private final BuildAddedListener buildAddedBroadcaster;
    private final BuildStateFactory buildStateFactory;

    // TODO: Locking around the following state
    private RootBuildState rootBuild;
    private final Map<BuildIdentifier, BuildState> buildsByIdentifier = new HashMap<>();
    private final Map<BuildState, StandAloneNestedBuild> buildSrcBuildsByOwner = new HashMap<>();
    private final Map<File, IncludedBuildState> includedBuildsByRootDir = new LinkedHashMap<>();
    private final Map<Path, File> includedBuildDirectoriesByPath = new LinkedHashMap<>();
    private final Deque<IncludedBuildState> pendingIncludedBuilds = new ArrayDeque<>();
    private boolean registerSubstitutionsForRootBuild = false;
    private final Set<IncludedBuildState> currentlyConfiguring = new HashSet<>();

    public DefaultIncludedBuildRegistry(IncludedBuildFactory includedBuildFactory, IncludedBuildDependencySubstitutionsBuilder dependencySubstitutionsBuilder, ListenerManager listenerManager, BuildStateFactory buildStateFactory) {
        this.includedBuildFactory = includedBuildFactory;
        this.dependencySubstitutionsBuilder = dependencySubstitutionsBuilder;
        this.buildAddedBroadcaster = listenerManager.getBroadcaster(BuildAddedListener.class);
        this.buildStateFactory = buildStateFactory;
    }

    @Override
    public RootBuildState getRootBuild() {
        if (rootBuild == null) {
            throw new IllegalStateException("Root build is not defined.");
        }
        return rootBuild;
    }

    @Override
    public RootBuildState createRootBuild(BuildDefinition buildDefinition) {
        if (rootBuild != null) {
            throw new IllegalStateException("Root build already defined.");
        }
        rootBuild = buildStateFactory.createRootBuild(buildDefinition);
        addBuild(rootBuild);
        return rootBuild;
    }

    @Override
    public void attachRootBuild(RootBuildState rootBuild) {
        if (this.rootBuild != null) {
            throw new IllegalStateException("Root build already defined.");
        }
        this.rootBuild = rootBuild;
        addBuild(rootBuild);
    }

    private void addBuild(BuildState build) {
        BuildState before = buildsByIdentifier.put(build.getBuildIdentifier(), build);
        if (before != null) {
            throw new IllegalArgumentException("Build is already registered: " + build.getBuildIdentifier());
        }
        buildAddedBroadcaster.buildAdded(build);
        maybeAddBuildSrcBuild(build);
    }

    @Override
    public IncludedBuildState addIncludedBuild(BuildDefinition buildDefinition) {
        return registerBuild(buildDefinition, false);
    }

    @Override
    public synchronized IncludedBuildState addImplicitIncludedBuild(BuildDefinition buildDefinition) {
        // TODO: synchronization with other methods
        IncludedBuildState includedBuild = includedBuildsByRootDir.get(buildDefinition.getBuildRootDir());
        if (includedBuild == null) {
            includedBuild = registerBuild(buildDefinition, true);
        }
        return includedBuild;
    }

    @Override
    public Collection<IncludedBuildState> getIncludedBuilds() {
        return includedBuildsByRootDir.values();
    }

    @Override
    public IncludedBuildState getIncludedBuild(BuildIdentifier buildIdentifier) {
        BuildState includedBuildState = buildsByIdentifier.get(buildIdentifier);
        if (!(includedBuildState instanceof IncludedBuildState)) {
            throw new IllegalArgumentException("Could not find " + buildIdentifier);
        }
        return (IncludedBuildState) includedBuildState;
    }

    @Override
    public BuildState getBuild(BuildIdentifier buildIdentifier) {
        BuildState buildState = buildsByIdentifier.get(buildIdentifier);
        if (buildState == null) {
            throw new IllegalArgumentException("Could not find " + buildIdentifier);
        }
        return buildState;
    }

    @Override
    public void afterConfigureRootBuild() {
        if (registerSubstitutionsForRootBuild) {
            dependencySubstitutionsBuilder.build(rootBuild);
        }
    }

    @Override
    public void finalizeIncludedBuilds() {
        while (!pendingIncludedBuilds.isEmpty()) {
            IncludedBuildState build = pendingIncludedBuilds.removeFirst();
            assertNameDoesNotClashWithRootSubproject(build);
        }
    }

    @Override
    public void registerSubstitutionsFor(IncludedBuildState build) {
        currentlyConfiguring.add(build);
        dependencySubstitutionsBuilder.build(build);
        currentlyConfiguring.remove(build);
    }

    @Override
    public StandAloneNestedBuild getBuildSrcNestedBuild(BuildState owner) {
        return buildSrcBuildsByOwner.get(owner);
    }

    private void maybeAddBuildSrcBuild(BuildState owner) {
        File buildSrcDir = new File(owner.getBuildRootDir(), SettingsInternal.BUILD_SRC);
        if (!BuildSrcDetector.isValidBuildSrcBuild(buildSrcDir)) {
            return;
        }

        BuildDefinition buildDefinition = buildStateFactory.buildDefinitionFor(buildSrcDir, owner);
        Path identityPath = assignPath(owner, buildDefinition.getName(), buildDefinition.getBuildRootDir());
        BuildIdentifier buildIdentifier = idFor(buildDefinition.getName());
        StandAloneNestedBuild build = buildStateFactory.createNestedBuild(buildIdentifier, identityPath, buildDefinition, owner);
        buildSrcBuildsByOwner.put(owner, build);
        addBuild(build);
    }

    @Override
    public NestedBuildTree addNestedBuildTree(BuildDefinition buildDefinition, BuildState owner, String buildName) {
        if (buildDefinition.getName() != null || buildDefinition.getBuildRootDir() != null) {
            throw new UnsupportedOperationException("Not yet implemented."); // but should be
        }
        File dir = buildDefinition.getStartParameter().getCurrentDir();
        String name = MoreObjects.firstNonNull(buildName, dir.getName());
        validateNameIsNotBuildSrc(name, dir);
        Path identityPath = assignPath(owner, name, dir);
        BuildIdentifier buildIdentifier = idFor(name);
        return buildStateFactory.createNestedTree(buildDefinition, buildIdentifier, identityPath, owner);
    }

    @Override
    public void registerSubstitutionsForRootBuild() {
        registerSubstitutionsForRootBuild = true;
    }

    @Override
    public void ensureConfigured(IncludedBuildState buildToConfigure) {
        if (currentlyConfiguring.contains(buildToConfigure)) {
            return;
        }
        currentlyConfiguring.add(buildToConfigure);
        buildToConfigure.ensureProjectsConfigured();
        GradleInternal gradle = buildToConfigure.getMutableModel();
        for (IncludedBuildInternal reference : gradle.includedBuilds()) {
            BuildState target = reference.getTarget();
            if (target instanceof IncludedBuildState) {
                dependencySubstitutionsBuilder.build((IncludedBuildState) target);
            }
        }
        currentlyConfiguring.remove(buildToConfigure);
    }

    @Override
    public void visitBuilds(Consumer<? super BuildState> visitor) {
        List<BuildState> ordered = new ArrayList<>(buildsByIdentifier.values());
        ordered.sort(Comparator.comparing(BuildState::getIdentityPath));
        for (BuildState buildState : ordered) {
            visitor.accept(buildState);
        }
    }

    private void validateNameIsNotBuildSrc(String name, File dir) {
        if (SettingsInternal.BUILD_SRC.equals(name)) {
            throw new GradleException("Included build " + dir + " has build name 'buildSrc' which cannot be used as it is a reserved name.");
        }
    }

    private IncludedBuildState registerBuild(BuildDefinition buildDefinition, boolean isImplicit) {
        // TODO: synchronization
        File buildDir = buildDefinition.getBuildRootDir();
        if (buildDir == null) {
            throw new IllegalArgumentException("Included build must have a root directory defined");
        }
        IncludedBuildState includedBuild = includedBuildsByRootDir.get(buildDir);
        if (includedBuild == null) {
            if (rootBuild == null) {
                throw new IllegalStateException("No root build attached yet.");
            }
            String buildName = buildDefinition.getName();
            if (buildName == null) {
                throw new IllegalStateException("build name is required");
            }
            validateNameIsNotBuildSrc(buildName, buildDir);
            Path idPath = assignPath(rootBuild, buildDefinition.getName(), buildDir);
            BuildIdentifier buildIdentifier = idFor(buildName);

            includedBuild = includedBuildFactory.createBuild(buildIdentifier, idPath, buildDefinition, isImplicit, rootBuild);
            includedBuildsByRootDir.put(buildDir, includedBuild);
            pendingIncludedBuilds.add(includedBuild);
            addBuild(includedBuild);
        } else {
            if (includedBuild.isImplicitBuild() != isImplicit) {
                throw new IllegalStateException("Unexpected state for build.");
            }
            // TODO: verify that the build definition is the same
        }
        return includedBuild;
    }

    private BuildIdentifier idFor(String buildName) {
        BuildIdentifier buildIdentifier = new DefaultBuildIdentifier(buildName);

        // Create a synthetic id for the build, if the id is already used
        // Should instead use a structured id implementation of some kind instead
        for (int count = 1; buildsByIdentifier.containsKey(buildIdentifier); count++) {
            buildIdentifier = new DefaultBuildIdentifier(buildName + ":" + count);
        }
        return buildIdentifier;
    }

    private Path assignPath(BuildState owner, String name, File dir) {
        Path requestedPath = owner.getIdentityPath().append(Path.path(name));
        File existingForPath = includedBuildDirectoriesByPath.putIfAbsent(requestedPath, dir);
        if (existingForPath != null) {
            throw new GradleException("Included build " + dir + " has build path " + requestedPath + " which is the same as included build " + existingForPath);
        }

        return requestedPath;
    }

    @Override
    public void stop() {
        CompositeStoppable.stoppable(buildsByIdentifier.values()).stop();
    }

    private void assertNameDoesNotClashWithRootSubproject(IncludedBuildState includedBuild) {
        if (rootBuild.getProjects().findProject(includedBuild.getIdentityPath()) != null) {
            throw new GradleException("Included build in " + includedBuild.getBuildRootDir() + " has name '" + includedBuild.getName() + "' which is the same as a project of the main build.");
        }
    }
}
