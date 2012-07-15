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
@Mojo(name = "pom")
public class SamplePomMojo extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        InputStream in = SamplePomMojo.class.getClassLoader().getResourceAsStream("sample_pom.xml");
        BufferedReader rdr = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while((line = rdr.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Sample POM file not found");
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("This POM assumes a file structure of src/main/jall/com/semperos for your JAll code. ");
        sb.append("It also assumes you have a Sample class, ");
        sb.append("which is configured to be run as a main class during the 'mvn test' phase. ");
        sb.append("\n");
        System.out.println(sb.toString());
    }
}
