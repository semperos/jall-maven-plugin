/*
 * Copyright (c) Daniel Gregoire 2012.
 *
 * The use and distribution terms for this software are covered by the Eclipse Public License 1.0
 * (http://opensource.org/licenses/eclipse-1.0.php) which can be found in the file epl-v10.html
 * at the root of this distribution.
 *
 * By using this software in any fashion, you are agreeing to be bound by the terms of this license.
 *
 * You must not remove this notice, or any other, from this software.
 */

package com.semperos.mojo.jall;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.INITIALIZE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class JAllCompilerMojo extends AbstractJAllCompilerMojo {

    /**
     * Should the compile phase create a temporary output directory for .class files?
     */
    // @Parameter(required = true, defaultValue = "false")
    // protected Boolean temporaryOutputDirectory;

    public void execute() throws MojoExecutionException {

//        File outputPath = outputDirectory;
        // if (temporaryOutputDirectory) {
        //     try {
        //         outputPath = File.createTempFile("classes", ".dir");
        //         getLog().debug("Compiling JAll sources to " + outputPath.getPath());
        //     } catch (IOException e) {
        //         throw new MojoExecutionException("Unable to create temporary output directory: " + e.getMessage());
        //     }
        //     outputPath.delete();
        //     outputPath.mkdir();
        // }

//        callJAllWith(
//                getSourceDirectories(SourceDirectory.COMPILE),
//                outputPath, classpathElements, "jall.core");
        callJAllWith();

        // copyNamespaceSourceFilesToOutput(outputDirectory, discoverNamespacesToCopy());
    }

}
