/*
 * Copyright 2010 the original author or authors.
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

package org.gradle.api.tasks.testing;

import com.google.common.collect.Lists;
import groovy.lang.Closure;
import org.gradle.StartParameter;
import org.gradle.api.Action;
import org.gradle.api.Incubating;
import org.gradle.api.JavaVersion;
import org.gradle.api.NonNullApi;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileTreeElement;
import org.gradle.api.internal.DocumentationRegistry;
import org.gradle.api.internal.classpath.ModuleRegistry;
import org.gradle.api.internal.tasks.testing.JvmTestExecutionSpec;
import org.gradle.api.internal.tasks.testing.TestExecuter;
import org.gradle.api.internal.tasks.testing.TestFramework;
import org.gradle.api.internal.tasks.testing.detection.DefaultTestExecuter;
import org.gradle.api.internal.tasks.testing.filter.DefaultTestFilter;
import org.gradle.api.internal.tasks.testing.junit.JUnitTestFramework;
import org.gradle.api.internal.tasks.testing.junit.result.TestClassResult;
import org.gradle.api.internal.tasks.testing.junit.result.TestResultSerializer;
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework;
import org.gradle.api.internal.tasks.testing.testng.TestNGTestFramework;
import org.gradle.api.internal.tasks.testing.worker.TestWorker;
import org.gradle.api.jvm.ModularitySpec;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.IgnoreEmptyDirectories;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SkipWhenEmpty;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.api.tasks.testing.junit.JUnitOptions;
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.internal.Actions;
import org.gradle.internal.Cast;
import org.gradle.internal.Factory;
import org.gradle.internal.actor.ActorFactory;
import org.gradle.internal.concurrent.CompositeStoppable;
import org.gradle.internal.deprecation.DeprecationLogger;
import org.gradle.internal.jvm.DefaultModularitySpec;
import org.gradle.internal.jvm.JavaModuleDetector;
import org.gradle.internal.jvm.Jvm;
import org.gradle.internal.jvm.UnsupportedJavaRuntimeException;
import org.gradle.internal.jvm.inspection.JvmVersionDetector;
import org.gradle.internal.scan.UsedByScanPlugin;
import org.gradle.internal.time.Clock;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.jvm.toolchain.JavaLauncher;
import org.gradle.process.CommandLineArgumentProvider;
import org.gradle.process.JavaDebugOptions;
import org.gradle.process.JavaForkOptions;
import org.gradle.process.ProcessForkOptions;
import org.gradle.process.internal.JavaForkOptionsFactory;
import org.gradle.process.internal.worker.WorkerProcessFactory;
import org.gradle.util.internal.ConfigureUtil;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkState;
import static org.gradle.util.internal.ConfigureUtil.configureUsing;

/**
 * Executes JUnit (3.8.x, 4.x or 5.x) or TestNG tests. Test are always run in (one or more) separate JVMs.
 *
 * <p>
 * The sample below shows various configuration options.
 *
 * <pre class='autoTested'>
 * plugins {
 *     id 'java' // adds 'test' task
 * }
 *
 * test {
 *   // Discover and execute JUnit4-based tests
 *   useJUnit()
 *
 *   // Discover and execute TestNG-based tests
 *   useTestNG()
 *
 *   // Discover and execute JUnit Platform-based tests
 *   useJUnitPlatform()
 *
 *   // set a system property for the test JVM(s)
 *   systemProperty 'some.prop', 'value'
 *
 *   // explicitly include or exclude tests
 *   include 'org/foo/**'
 *   exclude 'org/boo/**'
 *
 *   // show standard out and standard error of the test JVM(s) on the console
 *   testLogging.showStandardStreams = true
 *
 *   // set heap size for the test JVM(s)
 *   minHeapSize = "128m"
 *   maxHeapSize = "512m"
 *
 *   // set JVM arguments for the test JVM(s)
 *   jvmArgs '-XX:MaxPermSize=256m'
 *
 *   // listen to events in the test execution lifecycle
 *   beforeTest { descriptor -&gt;
 *      logger.lifecycle("Running test: " + descriptor)
 *   }
 *
 *   // Fail the 'test' task on the first test failure
 *   failFast = true
 *
 *   // listen to standard out and standard error of the test JVM(s)
 *   onOutput { descriptor, event -&gt;
 *      logger.lifecycle("Test: " + descriptor + " produced standard out/err: " + event.message )
 *   }
 * }
 * </pre>
 * <p>
 * The test process can be started in debug mode (see {@link #getDebug()}) in an ad-hoc manner by supplying the `--debug-jvm` switch when invoking the build.
 * <pre>
 * gradle someTestTask --debug-jvm
 * </pre>
 */
@NonNullApi
@CacheableTask
public class Test extends AbstractTestTask implements JavaForkOptions, PatternFilterable {

    private final JavaForkOptions forkOptions;
    private final ModularitySpec modularity;
    private final Property<JavaLauncher> javaLauncher;

    private FileCollection testClassesDirs;
    private final PatternFilterable patternSet;
    private FileCollection classpath;
    private final ConfigurableFileCollection stableClasspath;
    private final Property<TestFramework> testFramework;
    private boolean userHasConfiguredTestFramework;
    private boolean optionsAccessed;

    private boolean scanForTestClasses = true;
    private long forkEvery;
    private int maxParallelForks = 1;
    private TestExecuter<JvmTestExecutionSpec> testExecuter;

    public Test() {
        patternSet = getPatternSetFactory().create();
        classpath = getObjectFactory().fileCollection();
        // Create a stable instance to represent the classpath, that takes care of conventions and mutations applied to the property
        stableClasspath = getObjectFactory().fileCollection();
        stableClasspath.from(new Callable<Object>() {
            @Override
            public Object call() {
                return getClasspath();
            }
        });
        forkOptions = getForkOptionsFactory().newDecoratedJavaForkOptions();
        forkOptions.setEnableAssertions(true);
        forkOptions.setExecutable(null);
        modularity = getObjectFactory().newInstance(DefaultModularitySpec.class);
        javaLauncher = getObjectFactory().property(JavaLauncher.class);
        testFramework = getObjectFactory().property(TestFramework.class).convention(new JUnitTestFramework(this, (DefaultTestFilter) getFilter()));
    }

    @Inject
    protected ObjectFactory getObjectFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected ActorFactory getActorFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected WorkerProcessFactory getProcessBuilderFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected Factory<PatternSet> getPatternSetFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected JavaForkOptionsFactory getForkOptionsFactory() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected ModuleRegistry getModuleRegistry() {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected JavaModuleDetector getJavaModuleDetector() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Internal
    public File getWorkingDir() {
        return forkOptions.getWorkingDir();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkingDir(File dir) {
        forkOptions.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkingDir(Object dir) {
        forkOptions.setWorkingDir(dir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test workingDir(Object dir) {
        forkOptions.workingDir(dir);
        return this;
    }

    /**
     * Returns the version of Java used to run the tests based on the {@link JavaLauncher} specified by {@link #getJavaLauncher()},
     * or the executable specified by {@link #getExecutable()} if the {@code JavaLauncher} is not present.
     *
     * @since 3.3
     */
    @Input
    public JavaVersion getJavaVersion() {
        return getServices().get(JvmVersionDetector.class).getJavaVersion(getEffectiveExecutable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Internal
    public String getExecutable() {
        return forkOptions.getExecutable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test executable(Object executable) {
        forkOptions.executable(executable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExecutable(String executable) {
        forkOptions.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExecutable(Object executable) {
        forkOptions.setExecutable(executable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getSystemProperties() {
        return forkOptions.getSystemProperties();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSystemProperties(Map<String, ?> properties) {
        forkOptions.setSystemProperties(properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test systemProperties(Map<String, ?> properties) {
        forkOptions.systemProperties(properties);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test systemProperty(String name, Object value) {
        forkOptions.systemProperty(name, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileCollection getBootstrapClasspath() {
        return forkOptions.getBootstrapClasspath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBootstrapClasspath(FileCollection classpath) {
        forkOptions.setBootstrapClasspath(classpath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test bootstrapClasspath(Object... classpath) {
        forkOptions.bootstrapClasspath(classpath);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMinHeapSize() {
        return forkOptions.getMinHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultCharacterEncoding() {
        return forkOptions.getDefaultCharacterEncoding();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultCharacterEncoding(String defaultCharacterEncoding) {
        forkOptions.setDefaultCharacterEncoding(defaultCharacterEncoding);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMinHeapSize(String heapSize) {
        forkOptions.setMinHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMaxHeapSize() {
        return forkOptions.getMaxHeapSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxHeapSize(String heapSize) {
        forkOptions.setMaxHeapSize(heapSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getJvmArgs() {
        return forkOptions.getJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommandLineArgumentProvider> getJvmArgumentProviders() {
        return forkOptions.getJvmArgumentProviders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJvmArgs(List<String> arguments) {
        forkOptions.setJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJvmArgs(Iterable<?> arguments) {
        forkOptions.setJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test jvmArgs(Iterable<?> arguments) {
        forkOptions.jvmArgs(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test jvmArgs(Object... arguments) {
        forkOptions.jvmArgs(arguments);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getEnableAssertions() {
        return forkOptions.getEnableAssertions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnableAssertions(boolean enabled) {
        forkOptions.setEnableAssertions(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getDebug() {
        return forkOptions.getDebug();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Option(option = "debug-jvm", description = "Enable debugging for the test process. The process is started suspended and listening on port 5005.")
    public void setDebug(boolean enabled) {
        forkOptions.setDebug(enabled);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JavaDebugOptions getDebugOptions() {
        return forkOptions.getDebugOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debugOptions(Action<JavaDebugOptions> action) {
        forkOptions.debugOptions(action);
    }

    /**
     * Enables fail fast behavior causing the task to fail on the first failed test.
     */
    @Option(option = "fail-fast", description = "Stops test execution after the first failed test.")
    @Override
    public void setFailFast(boolean failFast) {
        super.setFailFast(failFast);
    }

    /**
     * Indicates if this task will fail on the first failed test
     *
     * @return whether this task will fail on the first failed test
     */
    @Override
    public boolean getFailFast() {
        return super.getFailFast();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllJvmArgs() {
        return forkOptions.getAllJvmArgs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAllJvmArgs(List<String> arguments) {
        forkOptions.setAllJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAllJvmArgs(Iterable<?> arguments) {
        forkOptions.setAllJvmArgs(arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Internal
    public Map<String, Object> getEnvironment() {
        return forkOptions.getEnvironment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test environment(Map<String, ?> environmentVariables) {
        forkOptions.environment(environmentVariables);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test environment(String name, Object value) {
        forkOptions.environment(name, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvironment(Map<String, ?> environmentVariables) {
        forkOptions.setEnvironment(environmentVariables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test copyTo(ProcessForkOptions target) {
        forkOptions.copyTo(target);
        copyToolchainAsExecutable(target);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test copyTo(JavaForkOptions target) {
        forkOptions.copyTo(target);
        copyToolchainAsExecutable(target);
        return this;
    }

    private void copyToolchainAsExecutable(ProcessForkOptions target) {
        target.setExecutable(getEffectiveExecutable());
    }

    /**
     * Returns the module path handling of this test task.
     *
     * @since 6.4
     */
    @Nested
    public ModularitySpec getModularity() {
        return modularity;
    }

    /**
     * {@inheritDoc}
     *
     * @since 4.4
     */
    @Override
    protected JvmTestExecutionSpec createTestExecutionSpec() {
        validateToolchainConfiguration();
        JavaForkOptions javaForkOptions = getForkOptionsFactory().newJavaForkOptions();
        copyTo(javaForkOptions);
        JavaModuleDetector javaModuleDetector = getJavaModuleDetector();
        boolean testIsModule = javaModuleDetector.isModule(modularity.getInferModulePath().get(), getTestClassesDirs());
        FileCollection classpath = javaModuleDetector.inferClasspath(testIsModule, stableClasspath);
        FileCollection modulePath = javaModuleDetector.inferModulePath(testIsModule, stableClasspath);
        return new JvmTestExecutionSpec(getTestFramework(), classpath, modulePath, getCandidateClassFiles(), isScanForTestClasses(), getTestClassesDirs(), getPath(), getIdentityPath(), getForkEvery(), javaForkOptions, getMaxParallelForks(), getPreviousFailedTestClasses());
    }

    private void validateToolchainConfiguration() {
        if (javaLauncher.isPresent()) {
            checkState(forkOptions.getExecutable() == null, "Must not use `executable` property on `Test` together with `javaLauncher` property");
        }
    }

    private Set<String> getPreviousFailedTestClasses() {
        TestResultSerializer serializer = new TestResultSerializer(getBinaryResultsDirectory().getAsFile().get());
        if (serializer.isHasResults()) {
            final Set<String> previousFailedTestClasses = new HashSet<String>();
            serializer.read(new Action<TestClassResult>() {
                @Override
                public void execute(TestClassResult testClassResult) {
                    if (testClassResult.getFailuresCount() > 0) {
                        previousFailedTestClasses.add(testClassResult.getClassName());
                    }
                }
            });
            return previousFailedTestClasses;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    @TaskAction
    public void executeTests() {
        JavaVersion javaVersion = getJavaVersion();
        if (!javaVersion.isJava6Compatible()) {
            throw new UnsupportedJavaRuntimeException("Support for test execution using Java 5 or earlier was removed in Gradle 3.0.");
        }
        if (!javaVersion.isJava8Compatible() && testFramework.get() instanceof JUnitPlatformTestFramework) {
            throw new UnsupportedJavaRuntimeException("Running tests with JUnit platform requires a Java 8+ toolchain.");
        }

        if (getDebug()) {
            getLogger().info("Running tests for remote debugging.");
        }
        forkOptions.systemProperty(TestWorker.WORKER_TMPDIR_SYS_PROPERTY, new File(getTemporaryDir(), "work"));

        try {
            super.executeTests();
        } finally {
            CompositeStoppable.stoppable(getTestFramework());
        }
    }

    @Override
    protected TestExecuter<JvmTestExecutionSpec> createTestExecuter() {
        if (testExecuter == null) {
            return new DefaultTestExecuter(getProcessBuilderFactory(), getActorFactory(), getModuleRegistry(),
                getServices().get(WorkerLeaseService.class),
                getServices().get(StartParameter.class).getMaxWorkerCount(),
                getServices().get(Clock.class),
                getServices().get(DocumentationRegistry.class),
                (DefaultTestFilter) getFilter());
        } else {
            return testExecuter;
        }
    }

    @Override
    protected List<String> getNoMatchingTestErrorReasons() {
        List<String> reasons = Lists.newArrayList();
        if (!getIncludes().isEmpty()) {
            reasons.add(getIncludes() + "(include rules)");
        }
        if (!getExcludes().isEmpty()) {
            reasons.add(getExcludes() + "(exclude rules)");
        }
        reasons.addAll(super.getNoMatchingTestErrorReasons());
        return reasons;
    }

    /**
     * Adds include patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see #setIncludes(Iterable)
     */
    @Override
    public Test include(String... includes) {
        patternSet.include(includes);
        return this;
    }

    /**
     * Adds include patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see #setIncludes(Iterable)
     */
    @Override
    public Test include(Iterable<String> includes) {
        patternSet.include(includes);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test include(Spec<FileTreeElement> includeSpec) {
        patternSet.include(includeSpec);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test include(Closure includeSpec) {
        patternSet.include(includeSpec);
        return this;
    }

    /**
     * Adds exclude patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see #setExcludes(Iterable)
     */
    @Override
    public Test exclude(String... excludes) {
        patternSet.exclude(excludes);
        return this;
    }

    /**
     * Adds exclude patterns for the files in the test classes directory (e.g. '**&#47;*Test.class')).
     *
     * @see #setExcludes(Iterable)
     */
    @Override
    public Test exclude(Iterable<String> excludes) {
        patternSet.exclude(excludes);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test exclude(Spec<FileTreeElement> excludeSpec) {
        patternSet.exclude(excludeSpec);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test exclude(Closure excludeSpec) {
        patternSet.exclude(excludeSpec);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Test setTestNameIncludePatterns(List<String> testNamePattern) {
        super.setTestNameIncludePatterns(testNamePattern);
        return this;
    }

    /**
     * Returns the directories for the compiled test sources.
     *
     * @return All test class directories to be used.
     * @since 4.0
     */
    @Internal
    public FileCollection getTestClassesDirs() {
        return testClassesDirs;
    }

    /**
     * Sets the directories to scan for compiled test sources.
     *
     * Typically, this would be configured to use the output of a source set:
     * <pre class='autoTested'>
     * plugins {
     *     id 'java'
     * }
     *
     * sourceSets {
     *    integrationTest {
     *       compileClasspath += main.output
     *       runtimeClasspath += main.output
     *    }
     * }
     *
     * task integrationTest(type: Test) {
     *     // Runs tests from src/integrationTest
     *     testClassesDirs = sourceSets.integrationTest.output.classesDirs
     *     classpath = sourceSets.integrationTest.runtimeClasspath
     * }
     * </pre>
     *
     * @param testClassesDirs All test class directories to be used.
     * @since 4.0
     */
    public void setTestClassesDirs(FileCollection testClassesDirs) {
        this.testClassesDirs = testClassesDirs;
    }

    /**
     * Returns the include patterns for test execution.
     *
     * @see #include(String...)
     */
    @Override
    @Internal
    public Set<String> getIncludes() {
        return patternSet.getIncludes();
    }

    /**
     * Sets the include patterns for test execution.
     *
     * @param includes The patterns list
     * @see #include(String...)
     */
    @Override
    public Test setIncludes(Iterable<String> includes) {
        patternSet.setIncludes(includes);
        return this;
    }

    /**
     * Returns the exclude patterns for test execution.
     *
     * @see #exclude(String...)
     */
    @Override
    @Internal
    public Set<String> getExcludes() {
        return patternSet.getExcludes();
    }

    /**
     * Sets the exclude patterns for test execution.
     *
     * @param excludes The patterns list
     * @see #exclude(String...)
     */
    @Override
    public Test setExcludes(Iterable<String> excludes) {
        patternSet.setExcludes(excludes);
        return this;
    }

    /**
     * Returns the chosen {@link TestFramework}.
     *
     * @since 7.3
     */
    @Incubating
    @Nested
    public Property<TestFramework> getTestFrameworkProperty() {
        return testFramework;
    }

    @Internal
    public TestFramework getTestFramework() {
        return testFramework(null);
    }

    public TestFramework testFramework(@Nullable Closure testFrameworkConfigure) {
        if (!testFramework.isPresent()) {
            useJUnit(testFrameworkConfigure);
        }

        // To maintain backwards compatibility with builds that may configure the test framework
        // multiple times for a single task--either switching between frameworks or overwriting
        // the existing configuration for a test framework--we need to keep track if the user has
        // explicitly set a test framework
        // With test suites, users should never need to call the useXXX methods, so we can warn if
        // them from doing something like this (for now, in order to preserve existing builds).
        // This behavior should be restored to fail fast once again with the next major version.
        //
        // testTask.configure {
        //      options {
        //          /* configure JUnit Platform */
        //      }
        // }
        // testTask.configure {
        //      useJUnit()
        //      options {
        //          /* configure JUnit */
        //      }
        // }

        // TODO: Test Framework Selection - Restore this to re-enable fail-fast behavior for Gradle 8
//        if (!userHasConfiguredTestFramework) {
//            testFramework.finalizeValue();
//        }

        return testFramework.get();
    }

    /**
     * Returns test framework specific options. Make sure to call {@link #useJUnit()}, {@link #useJUnitPlatform()} or {@link #useTestNG()} before using this method.
     *
     * @return The test framework options.
     */
    @Nested
    public TestFrameworkOptions getOptions() {
        optionsAccessed = true;
        return getTestFramework().getOptions();
    }

    /**
     * Configures test framework specific options. Make sure to call {@link #useJUnit()}, {@link #useJUnitPlatform()} or {@link #useTestNG()} before using this method.
     * <p>
     * Any previous option configuration will be <strong>DISCARDED</strong> upon changing the test framework.  Accessing options prior to setting the test framework will be
     * deprecated in Gradle 8.
     *
     * @return The test framework options.
     */
    public TestFrameworkOptions options(Closure testFrameworkConfigure) {
        return ConfigureUtil.configure(testFrameworkConfigure, getOptions());
    }

    /**
     * Configures test framework specific options. Make sure to call {@link #useJUnit()}, {@link #useJUnitPlatform()} or {@link #useTestNG()} before using this method.
     *
     * @return The test framework options.
     * @since 3.5
     */
    public TestFrameworkOptions options(Action<? super TestFrameworkOptions> testFrameworkConfigure) {
        TestFrameworkOptions options = getOptions();
        testFrameworkConfigure.execute(options);
        return options;
    }

    TestFramework useTestFramework(TestFramework testFramework) {
        return useTestFramework(testFramework, null);
    }

    private <T extends TestFrameworkOptions> TestFramework useTestFramework(TestFramework testFramework, @Nullable Action<? super T> testFrameworkConfigure) {
        if (optionsAccessed) {
            DeprecationLogger.deprecateAction("Accessing test options prior to setting test framework")
                .withContext("\nTest options have already been accessed for task: '" + getProject().getName() + ":" + getName() + "' prior to setting the test framework to: '" + testFramework.getClass().getSimpleName() + "'. Any previous configuration will be discarded.\n")
                .willBeRemovedInGradle8()
                .withDslReference(Test.class, "options")
                .nagUser();

            if (!this.testFramework.get().getClass().equals(testFramework.getClass())) {
                getLogger().warn("Test framework is changing from '{}', previous option configuration would not be applicable.", this.testFramework.get().getClass().getSimpleName());
            }
        }

        userHasConfiguredTestFramework = true;
        this.testFramework.set(testFramework);

        if (testFrameworkConfigure != null) {
            testFrameworkConfigure.execute(Cast.<T>uncheckedNonnullCast(this.testFramework.get().getOptions()));
        }

        return this.testFramework.get();
    }

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests.
     * <p>
     * @see #useJUnit(org.gradle.api.Action) Configure JUnit4 specific options.
     */
    public void useJUnit() {
        useJUnit(Actions.<JUnitOptions>doNothing());
    }

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests with additional configuration.
     * <p>
     * The supplied action configures an instance of {@link org.gradle.api.tasks.testing.junit.JUnitOptions JUnit4 specific options}.
     *
     * @param testFrameworkConfigure A closure used to configure JUnit4 options.
     */
    public void useJUnit(@Nullable Closure testFrameworkConfigure) {
        useJUnit(ConfigureUtil.<JUnitOptions>configureUsing(testFrameworkConfigure));
    }

    /**
     * Specifies that JUnit4 should be used to discover and execute the tests with additional configuration.
     * <p>
     * The supplied action configures an instance of {@link org.gradle.api.tasks.testing.junit.JUnitOptions JUnit4 specific options}.
     *
     * @param testFrameworkConfigure An action used to configure JUnit4 options.
     * @since 3.5
     */
    public void useJUnit(Action<? super JUnitOptions> testFrameworkConfigure) {
        useTestFramework(new JUnitTestFramework(this, (DefaultTestFilter) getFilter()), testFrameworkConfigure);
    }

    /**
     * Specifies that JUnit Platform should be used to discover and execute the tests.
     * <p>
     * Use this option if your tests use JUnit Jupiter/JUnit5.
     * <p>
     * JUnit Platform supports multiple test engines, which allows other testing frameworks to be built on top of it.
     * You may need to use this option even if you are not using JUnit directly.
     *
     * @since 4.6
     * @see #useJUnitPlatform(org.gradle.api.Action) Configure JUnit Platform specific options.
     */
    public void useJUnitPlatform() {
        useJUnitPlatform(Actions.<JUnitPlatformOptions>doNothing());
    }

    /**
     * Specifies that JUnit Platform should be used to discover and execute the tests with additional configuration.
     * <p>
     * Use this option if your tests use JUnit Jupiter/JUnit5.
     * <p>
     * JUnit Platform supports multiple test engines, which allows other testing frameworks to be built on top of it.
     * You may need to use this option even if you are not using JUnit directly.
     * <p>
     * The supplied action configures an instance of {@link org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions JUnit Platform specific options}.
     *
     * @param testFrameworkConfigure A closure used to configure JUnit platform options.
     * @since 4.6
     */
    public void useJUnitPlatform(Action<? super JUnitPlatformOptions> testFrameworkConfigure) {
        useTestFramework(new JUnitPlatformTestFramework((DefaultTestFilter) getFilter()), testFrameworkConfigure);
    }

    /**
     * Specifies that TestNG should be used to discover and execute the tests.
     * <p>
     * @see #useTestNG(org.gradle.api.Action) Configure TestNG specific options.
     */
    public void useTestNG() {
        useTestNG(Actions.<TestFrameworkOptions>doNothing());
    }

    /**
     * Specifies that TestNG should be used to discover and execute the tests with additional configuration.
     * <p>
     * The supplied action configures an instance of {@link org.gradle.api.tasks.testing.testng.TestNGOptions TestNG specific options}.
     *
     * @param testFrameworkConfigure A closure used to configure TestNG options.
     */
    public void useTestNG(Closure testFrameworkConfigure) {
        useTestNG(configureUsing(testFrameworkConfigure));
    }

    /**
     * Specifies that TestNG should be used to discover and execute the tests with additional configuration.
     * <p>
     * The supplied action configures an instance of {@link org.gradle.api.tasks.testing.testng.TestNGOptions TestNG specific options}.
     *
     * @param testFrameworkConfigure An action used to configure TestNG options.
     * @since 3.5
     */
    public void useTestNG(Action<? super TestNGOptions> testFrameworkConfigure) {
        useTestFramework(new TestNGTestFramework(this, stableClasspath, (DefaultTestFilter) getFilter(), getObjectFactory()), testFrameworkConfigure);
    }

    /**
     * Returns the classpath to use to execute the tests.
     *
     * @since 6.6
     */
    @Classpath
    protected FileCollection getStableClasspath() {
        return stableClasspath;
    }

    /**
     * Returns the classpath to use to execute the tests.
     */
    @Internal("captured by stableClasspath")
    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

    /**
     * Specifies whether test classes should be detected. When {@code true} the classes which match the include and exclude patterns are scanned for test classes, and any found are executed. When
     * {@code false} the classes which match the include and exclude patterns are executed.
     */
    @Input
    public boolean isScanForTestClasses() {
        return scanForTestClasses;
    }

    public void setScanForTestClasses(boolean scanForTestClasses) {
        this.scanForTestClasses = scanForTestClasses;
    }

    /**
     * Returns the maximum number of test classes to execute in a forked test process. The forked test process will be restarted when this limit is reached.
     *
     * <p>
     * By default, Gradle automatically uses a separate JVM when executing tests.
     * <ul>
     *  <li>A value of <code>0</code> (no limit) means to reuse the test process for all test classes. This is the default.</li>
     *  <li>A value of <code>1</code> means that a new test process is started for <b>every</b> test class. <b>This is very expensive.</b></li>
     *  <li>A value of <code>N</code> means that a new test process is started after <code>N</code> test classes.</li>
     * </ul>
     * This property can have a large impact on performance due to the cost of stopping and starting each test process. It is unusual for this property to be changed from the default.
     *
     * @return The maximum number of test classes to execute in a test process. Returns 0 when there is no maximum.
     */
    @Internal
    public long getForkEvery() {
        return getDebug() ? 0 : forkEvery;
    }

    /**
     * Sets the maximum number of test classes to execute in a forked test process.
     * <p>
     * By default, Gradle automatically uses a separate JVM when executing tests, so changing this property is usually not necessary.
     * </p>
     *
     * @param forkEvery The maximum number of test classes. Use null or 0 to specify no maximum.
     */
    public void setForkEvery(@Nullable Long forkEvery) {
        if (forkEvery != null && forkEvery < 0) {
            throw new IllegalArgumentException("Cannot set forkEvery to a value less than 0.");
        }
        this.forkEvery = forkEvery == null ? 0 : forkEvery;
    }

    /**
     * Returns the maximum number of test processes to start in parallel.
     *
     * <p>
     * By default, Gradle executes a single test class at a time.
     * <ul>
     * <li>A value of <code>1</code> means to only execute a single test class in a single test process at a time. This is the default.</li>
     * <li>A value of <code>N</code> means that up to <code>N</code> test processes will be started to execute test classes. <b>This can improve test execution time by running multiple test classes in parallel.</b></li>
     * </ul>
     *
     * This property cannot exceed the value of {@literal max-workers} for the current build. Gradle will also limit the number of started test processes across all {@link Test} tasks.
     *
     * @return The maximum number of forked test processes.
     */
    @Internal
    public int getMaxParallelForks() {
        return getDebug() ? 1 : maxParallelForks;
    }

    /**
     * Sets the maximum number of test processes to start in parallel.
     * <p>
     * By default, Gradle executes a single test class at a time but allows multiple {@link Test} tasks to run in parallel.
     * </p>
     *
     * @param maxParallelForks The maximum number of forked test processes. Use 1 to disable parallel test execution for this task.
     */
    public void setMaxParallelForks(int maxParallelForks) {
        if (maxParallelForks < 1) {
            throw new IllegalArgumentException("Cannot set maxParallelForks to a value less than 1.");
        }
        this.maxParallelForks = maxParallelForks;
    }

    /**
     * Returns the classes files to scan for test classes.
     *
     * @return The candidate class files.
     */
    @InputFiles
    @SkipWhenEmpty
    @IgnoreEmptyDirectories
    @PathSensitive(PathSensitivity.RELATIVE)
    public FileTree getCandidateClassFiles() {
        return getTestClassesDirs().getAsFileTree().matching(patternSet);
    }

    /**
     * Executes the action against the {@link #getFilter()}.
     *
     * @param action configuration of the test filter
     * @since 1.10
     */
    public void filter(Action<TestFilter> action) {
        action.execute(getFilter());
    }

    /**
     * Sets the testExecuter property.
     *
     * @since 4.2
     */
    @UsedByScanPlugin("test-distribution")
    void setTestExecuter(TestExecuter<JvmTestExecutionSpec> testExecuter) {
        this.testExecuter = testExecuter;
    }

    /**
     * Configures the java executable to be used to run the tests.
     *
     * @since 6.7
     */
    @Nested
    @Optional
    public Property<JavaLauncher> getJavaLauncher() {
        return javaLauncher;
    }

    private String getEffectiveExecutable() {
        if (javaLauncher.isPresent()) {
            // The below line is OK because it will only be exercised in the Gradle daemon and not in the worker running tests.
            return javaLauncher.get().getExecutablePath().toString();
        }
        final String executable = getExecutable();
        return executable == null ? Jvm.current().getJavaExecutable().getAbsolutePath() : executable;
    }

}
