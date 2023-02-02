/*
 * Copyright 2011 the original author or authors.
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

package org.gradle.plugins.ear;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.artifacts.dsl.LazyPublishArtifact;
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.jvm.internal.JvmPluginServices;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.plugins.ear.descriptor.DeploymentDescriptor;

import javax.inject.Inject;
import java.util.concurrent.Callable;

/**
 * <p>
 * A {@link Plugin} with tasks which assemble a web application into a EAR file.
 * </p>
 *
 * @see <a href="https://docs.gradle.org/current/userguide/ear_plugin.html">EAR plugin reference</a>
 */
@SuppressWarnings("deprecation")
public class EarPlugin implements Plugin<Project> {

    public static final String EAR_TASK_NAME = "ear";

    public static final String DEPLOY_CONFIGURATION_NAME = "deploy";
    public static final String EARLIB_CONFIGURATION_NAME = "earlib";

    static final String DEFAULT_LIB_DIR_NAME = "lib";

    private final ObjectFactory objectFactory;
    private final JvmPluginServices jvmPluginServices;

    /**
     * Injects an {@link ObjectFactory}
     *
     * @since 4.2
     */
    @Inject
    public EarPlugin(ObjectFactory objectFactory, JvmPluginServices jvmPluginServices) {
        this.objectFactory = objectFactory;
        this.jvmPluginServices = jvmPluginServices;
    }

    @Override
    public void apply(final Project project) {
        project.getPluginManager().apply(BasePlugin.class);

        EarPluginConvention earPluginConvention = objectFactory.newInstance(org.gradle.plugins.ear.internal.DefaultEarPluginConvention.class);
        project.getConvention().getPlugins().put("ear", earPluginConvention);
        earPluginConvention.setLibDirName(DEFAULT_LIB_DIR_NAME);
        earPluginConvention.setAppDirName("src/main/application");

        configureConfigurations(project);

        PluginContainer plugins = project.getPlugins();
        setupEarTask(project, earPluginConvention, plugins);
        wireEarTaskConventions(project, earPluginConvention);
        wireEarTaskConventionsWithJavaPluginApplied(project, plugins);
    }

    private void wireEarTaskConventionsWithJavaPluginApplied(final Project project, PluginContainer plugins) {
        plugins.withType(JavaPlugin.class, javaPlugin -> {
            final JavaPluginExtension javaPluginExtension = project.getExtensions().findByType(JavaPluginExtension.class);
            project.getTasks().withType(Ear.class).configureEach(task -> {
                task.dependsOn((Callable<FileCollection>) () ->
                    mainSourceSetOf(javaPluginExtension).getRuntimeClasspath()
                );
                task.from((Callable<FileCollection>) () ->
                    mainSourceSetOf(javaPluginExtension).getOutput()
                );
            });
        });
    }

    private SourceSet mainSourceSetOf(JavaPluginExtension javaPluginExtension) {
        return javaPluginExtension.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
    }

    private void setupEarTask(final Project project, EarPluginConvention convention, PluginContainer plugins) {
        TaskProvider<Ear> ear = project.getTasks().register(EAR_TASK_NAME, Ear.class, new Action<Ear>() {
            @Override
            public void execute(Ear ear) {
                ear.setDescription("Generates a ear archive with all the modules, the application descriptor and the libraries.");
                ear.setGroup(BasePlugin.BUILD_GROUP);
                ear.getGenerateDeploymentDescriptor().convention(convention.getGenerateDeploymentDescriptor());

                plugins.withType(JavaPlugin.class, javaPlugin -> {
                    final JavaPluginExtension javaPluginExtension = project.getExtensions().findByType(JavaPluginExtension.class);
                    SourceSet sourceSet = mainSourceSetOf(javaPluginExtension);
                    sourceSet.getResources().srcDir(ear.getAppDirectory());
                });
            }
        });

        DeploymentDescriptor deploymentDescriptor = convention.getDeploymentDescriptor();
        if (deploymentDescriptor != null) {
            if (deploymentDescriptor.getDisplayName() == null) {
                deploymentDescriptor.setDisplayName(project.getName());
            }
            if (deploymentDescriptor.getDescription() == null) {
                deploymentDescriptor.setDescription(project.getDescription());
            }
        }
        project.getExtensions().getByType(DefaultArtifactPublicationSet.class).addCandidate(new LazyPublishArtifact(ear, ((ProjectInternal) project).getFileResolver()));

        project.getTasks().withType(Ear.class).configureEach(new Action<Ear>() {
            @Override
            public void execute(Ear task) {

            }
        });
    }

    private void wireEarTaskConventions(Project project, final EarPluginConvention earConvention) {
        project.getTasks().withType(Ear.class).configureEach(new Action<Ear>() {
            @Override
            public void execute(Ear task) {
                task.getAppDirectory().convention(project.provider(() -> project.getLayout().getProjectDirectory().dir(earConvention.getAppDirName())));
                task.getConventionMapping().map("libDirName", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return earConvention.getLibDirName();
                    }
                });
                task.getConventionMapping().map("deploymentDescriptor", new Callable<DeploymentDescriptor>() {
                    @Override
                    public DeploymentDescriptor call() throws Exception {
                        return earConvention.getDeploymentDescriptor();
                    }
                });

                task.from((Callable<FileCollection>) () -> {
                    if (project.getPlugins().hasPlugin(JavaPlugin.class)) {
                        return null;
                    } else {
                        return task.getAppDirectory().getAsFileTree();
                    }
                });

                task.getLib().from(new Callable<FileCollection>() {
                    @Override
                    public FileCollection call() throws Exception {
                        // Ensure that deploy jars are not also added into lib folder.
                        // Allows the user to get transitive dependencies for a bean artifact by adding it to both earlib and deploy but only having the file once in the ear.
                        return project.getConfigurations().getByName(EARLIB_CONFIGURATION_NAME)
                            .minus(project.getConfigurations().getByName(DEPLOY_CONFIGURATION_NAME));
                    }
                });
                task.from(new Callable<FileCollection>() {
                    @Override
                    public FileCollection call() throws Exception {
                        // add the module configuration's files
                        return project.getConfigurations().getByName(DEPLOY_CONFIGURATION_NAME);
                    }
                });
            }
        });
    }

    private void configureConfigurations(final Project project) {
        // Currently 'deploy' and 'earlib' are both _resolvable_ and _consumable_.
        // In the future, it would be good to split these so that the attributes can differ.
        // Then 'jvmPluginServices.configureAsRuntimeClasspath()' may be used to configure the resolving configurations.
        ConfigurationContainer configurations = project.getConfigurations();
        Configuration moduleConfiguration = configurations.create(DEPLOY_CONFIGURATION_NAME).setVisible(false)
            .setTransitive(false).setDescription("Classpath for deployable modules, not transitive.");
        jvmPluginServices.configureAttributes(moduleConfiguration, details -> details.library().runtimeUsage().withExternalDependencies());
        Configuration earlibConfiguration = configurations.create(EARLIB_CONFIGURATION_NAME).setVisible(false)
            .setDescription("Classpath for module dependencies.");
        jvmPluginServices.configureAttributes(earlibConfiguration, details -> details.library().runtimeUsage().withExternalDependencies());
        configurations.getByName(Dependency.DEFAULT_CONFIGURATION)
            .extendsFrom(moduleConfiguration, earlibConfiguration);
    }
}
