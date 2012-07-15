/**
 *   Copyright (c) Daniel Gregoire. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

package com.semperos.mojo.jall;

import com.semperos.jall.lang.Compiler;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractJAllCompilerMojo extends AbstractMojo {

    @Parameter(required = true, readonly = true, property = "project")
    protected MavenProject project;

    @Component
    private ToolchainManager toolchainManager;

    @Parameter(required = true, readonly = true, property = "session")
    private MavenSession session;

    @Parameter(required = true, readonly = true, property = "basedir")
    protected File baseDirectory;

    @Parameter(required = true, readonly = true, property = "project.compileClasspathElements")
    protected List<String> classpathElements;

    @Parameter(required = true, readonly = true, property = "project.testClasspathElements")
    protected List<String> testClasspathElements;

    @Parameter(required = true, defaultValue = "${project.build.outputDirectory}")
    protected File outputDirectory;

    @Parameter(required = true, defaultValue = "${project.build.testOutputDirectory}")
    protected File testOutputDirectory;

    /**
     * Location of the source files.
     */
    @Parameter
    protected String[] sourceDirectories = new String[]{"src/main/jall"};

    /**
     * Location of the source files.
     */
    @Parameter
    protected String[] testSourceDirectories = new String[]{"src/test/jall"};

    /**
     * Location of the source files.
     */
    @Parameter(required = true, defaultValue = "${project.build.testSourceDirectory}")
    protected File baseTestSourceDirectory;

    /**
     * Location of the generated source files.
     */
    @Parameter(required = true, defaultValue = "${project.build.outputDirectory}/../generated-sources")
    protected File generatedSourceDirectory;

    /**
     * Working directory for forked java JAll process.
     */
    @Parameter
    protected File workingDirectory;

    /**
     * Should we compile all namespaces or only those defined?
     */
//    @Parameter(defaultValue = "false")
//    protected boolean compileDeclaredNamespaceOnly;

    /**
     * A list of namespaces to compile
     */
//    @Parameter
//    protected String[] namespaces;

    /**
     * Should we test all namespaces or only those defined?
     */
//    @Parameter(defaultValue = "false")
//    protected boolean testDeclaredNamespaceOnly;

    /**
     * A list of test namespaces to compile
     */
//    @Parameter
//    protected String[] testNamespaces;

    /**
     * Classes to put onto the command line before the main class
     */
//    @Parameter
//    private List<String> prependClasses;

    /**
     * JAll/Java command-line options
     */
    @Parameter(property = "jall.options")
    private String jallOptions = "";

    /**
     * Run with test-classpath or compile-classpath?
     */
//    @Parameter(property = "jall.runwith.test", defaultValue = "true")
//    private boolean runWithTests;

    /**
     * A list of namespaces whose source files will be copied to the output.
     */
//    @Parameter
//    protected String[] copiedNamespaces;

    /**
     * Should we copy the source of all namespaces or only those defined?
     */
//    @Parameter(defaultValue = "false")
//    protected boolean copyDeclaredNamespaceOnly;

    /**
     * Should the source files of all compiled namespaces be copied to the output?
     * This overrides copiedNamespaces and copyDeclaredNamespaceOnly.
     */
//    @Parameter(defaultValue = "false")
//    private boolean copyAllCompiledNamespaces;

    /**
     * Should reflective invocations in Clojure source emit warnings?  Corresponds with
     * the *warn-on-reflection* var and the clojure.compile.warn-on-reflection system property.
     */
//    @Parameter(defaultValue = "false")
//    private boolean warnOnReflection;

    /**
     * Specify additional vmargs to use when running clojure or swank.
     */
//    @Parameter(property = "jall.vmargs")
//    private String vmargs;


    /**
     * Spawn a new console window for interactive clojure sessions on Windows
     */
//    @Parameter(defaultValue = "true")
//    private boolean spawnInteractiveConsoleOnWindows;

    /**
     * Escapes the given file path so that it's safe for inclusion in a
     * Clojure string literal.
     *
     * @param directory directory path
     * @param file      file name
     * @return escaped file path, ready for inclusion in a string literal
     */
    protected String escapeFilePath(String directory, String file) {
        return escapeFilePath(new File(directory, file));
    }

    /**
     * Escapes the given file path so that it's safe for inclusion in a
     * Clojure string literal.
     *
     * @param file
     * @return escaped file path, ready for inclusion in a string literal
     */
    protected String escapeFilePath(final File file) {
        // TODO: Should handle also possible newlines, etc.
        return file.getPath().replace("\\", "\\\\");
    }

    private String getJavaExecutable() throws MojoExecutionException {

        Toolchain tc = toolchainManager.getToolchainFromBuildContext("jdk", //NOI18N
                                                                     session);
        if (tc != null) {
            getLog().info("Toolchain in jall-maven-plugin: " + tc);
            String foundExecutable = tc.findTool("java");
            if (foundExecutable != null) {
                return foundExecutable;
            } else {
                throw new MojoExecutionException("Unable to find 'java' executable for toolchain: " + tc);
            }
        }

        return "java";
    }

    protected File getWorkingDirectory() throws MojoExecutionException {
        if (workingDirectory != null) {
            if (workingDirectory.exists()) {
                return workingDirectory;
            } else {
                throw new MojoExecutionException("Directory specified in <workingDirectory/> does not exists: " + workingDirectory.getPath());
            }
        } else {
            return session.getCurrentProject().getBasedir();
        }
    }

    private File[] translatePaths(String[] paths) {
        File[] files = new File[paths.length];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(baseDirectory, paths[i]);
        }
        return files;
    }

    public enum SourceDirectory {
        COMPILE, TEST
    }

    public String getSourcePath(SourceDirectory... sourceDirectoryTypes) {
        return getPath(getSourceDirectories(sourceDirectoryTypes));
    }

    public File[] getSourceDirectories(SourceDirectory... sourceDirectoryTypes) {
        List<File> dirs = new ArrayList<File>();

        if (Arrays.asList(sourceDirectoryTypes).contains(SourceDirectory.COMPILE)) {
            dirs.add(generatedSourceDirectory);
            dirs.addAll(Arrays.asList(translatePaths(sourceDirectories)));
        }
        if (Arrays.asList(sourceDirectoryTypes).contains(SourceDirectory.TEST)) {
            dirs.add(baseTestSourceDirectory);
            dirs.addAll(Arrays.asList(translatePaths(testSourceDirectories)));
        }

        return dirs.toArray(new File[]{});

    }

    private String getPath(File[] sourceDirectory) {
        String cp = "";
        for (File directory : sourceDirectory) {
            cp = cp + directory.getPath() + File.pathSeparator;
        }
        return cp.substring(0, cp.length() - 1);
    }

//    public List<String> getRunWithClasspathElements() {
//        return runWithTests ? testClasspathElements : classpathElements;
//    }

//    protected void callJAllWith(
//            File[] sourceDirectory,
//            File outputDirectory,
//            List<String> compileClasspathElements,
//            String mainClass) throws MojoExecutionException {
//        callJAllWith(ExecutionMode.BATCH, sourceDirectory, outputDirectory, compileClasspathElements, mainClass);
//    }

//    protected void callJAllWith(
//            File[] sourceDirectory,
//            File outputDirectory,
//            List<String> compileClasspathElements,
//            String mainClass,
//            String[] jallArgs) throws MojoExecutionException {
//        callJAllWith(ExecutionMode.BATCH, sourceDirectory, outputDirectory, compileClasspathElements, mainClass, jallArgs);
//    }

    protected void callJAllWith() throws MojoExecutionException {
        Compiler.processSrcDir(baseDirectory.toString(), sourceDirectories[0]);
//        final String javaExecutable = getJavaExecutable();
//        getLog().debug("Java exectuable used:  " + javaExecutable);
//        CommandLine cl = new CommandLine(javaExecutable);
//
//        // Run as executable JAR
//        cl.addArgument("-jar");
//
//        // JAll options
//        cl.addArguments(jallOptions, false);
//
//        getLog().debug("Command line: " + cl.toString());
//
//        Executor exec = new DefaultExecutor();
//        Map<String, String> env = new HashMap<String, String>(System.getenv());
//
//        ExecuteStreamHandler handler = new PumpStreamHandler(System.out, System.err, System.in);
//        exec.setStreamHandler(handler);
//        exec.setWorkingDirectory(getWorkingDirectory());
//
//        int status;
//        try {
//            status = exec.execute(cl, env);
//        } catch (ExecuteException e) {
//            status = e.getExitValue();
//        } catch (IOException e) {
//            status = 1;
//        }
//
//        if (status != 0) {
//            throw new MojoExecutionException("JAll failed.");
//        }
    }

//    protected void callJAllWith(
//            ExecutionMode executionMode,
//            File[] sourceDirectory,
//            File outputDirectory,
//            List<String> compileClasspathElements,
//            String mainClass,
//            String[] jallArgs) throws MojoExecutionException {
//
//        outputDirectory.mkdirs();
//
//        String cp = getPath(sourceDirectory);
//
//        cp = cp + File.pathSeparator + outputDirectory.getPath() + File.pathSeparator;
//
//        for (Object classpathElement : compileClasspathElements) {
//            cp = cp + File.pathSeparator + classpathElement;
//        }
//
//        cp = cp.replaceAll("\\s", "\\ ");
//
//        final String javaExecutable = getJavaExecutable();
//        getLog().debug("Java exectuable used:  " + javaExecutable);
//        getLog().debug("JAll classpath: " + cp);
//        CommandLine cl = null;
//
//        if (ExecutionMode.INTERACTIVE == executionMode && SystemUtils.IS_OS_WINDOWS && spawnInteractiveConsoleOnWindows) {
//            cl = new CommandLine("cmd");
//            cl.addArgument("/c");
//            cl.addArgument("start");
//            cl.addArgument(javaExecutable);
//        } else {
//            cl = new CommandLine(javaExecutable);
//        }
//
//        if (vmargs != null) {
//            cl.addArguments(vmargs, false);
//        }
//
//        cl.addArgument("-cp");
//        cl.addArgument(cp, false);
//        cl.addArgument("-Djall.compile.path=" + escapeFilePath(outputDirectory), false);
//
//        if (warnOnReflection) cl.addArgument("-Djall.compile.warn-on-reflection=true");
//
//        cl.addArguments(jallOptions, false);
//
//        if (prependClasses != null) {
//            cl.addArguments(prependClasses.toArray(new String[prependClasses.size()]));
//        }
//
//        cl.addArgument(mainClass);
//
//        if (jallArgs != null) {
//            cl.addArguments(jallArgs, false);
//        }
//
//        getLog().debug("Command line: " + cl.toString());
//
//        Executor exec = new DefaultExecutor();
//        Map<String, String> env = new HashMap<String, String>(System.getenv());
////        env.put("path", ";");
////        env.put("path", System.getProperty("java.home"));
//
//        ExecuteStreamHandler handler = new PumpStreamHandler(System.out, System.err, System.in);
//        exec.setStreamHandler(handler);
//        exec.setWorkingDirectory(getWorkingDirectory());
//
//        int status;
//        try {
//            status = exec.execute(cl, env);
//        } catch (ExecuteException e) {
//            status = e.getExitValue();
//        } catch (IOException e) {
//            status = 1;
//        }
//
//        if (status != 0) {
//            throw new MojoExecutionException("JAll failed.");
//        }
//
//    }

}



