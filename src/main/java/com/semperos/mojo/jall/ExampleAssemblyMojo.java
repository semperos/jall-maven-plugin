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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Print out a sample, working POM to use with a JAll project.
 */
@Mojo(name = "assembly")
public class ExampleAssemblyMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        InputStream in = ExamplePomMojo.class.getClassLoader().getResourceAsStream("example_assembly.xml");
        BufferedReader rdr = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while((line = rdr.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Sample assembly.xml file not found");
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("This assembly.xml file works in concert with the example pom.xml file.\n");
        sb.append("You should put it at the root of your project, at the same level as your POM.\n");
        sb.append("It includes all Java and JRuby dependencies in a single, executable JAR\n");
        sb.append("that uses the same 'mainClass' as the 'exampleMainClass' property in\n");
        sb.append("your POM file.\n");
        sb.append("\n");
        sb.append("To generate the executable JAR with all dependencies, run the following,\n");
        sb.append("optionally changing the value of 'exampleMainClass':\n");
        sb.append("mvn package \\\n");
        sb.append("  -Pexecutable-with-dependencies \\\n");
        sb.append("  -DexampleMainClass=com.semperos.IntermediateExample\n");
        sb.append("\n");
        System.out.println(sb.toString());
    }
}
