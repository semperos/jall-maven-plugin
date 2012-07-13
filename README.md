# JAll Maven Plugin #

JAll is a powerful templating language for Java that allows you to write code for JRuby, Clojure and soon Scala *inside* your Java source code. This is the Maven plugin to make working with JAll and compiling multiple JVM languages under one roof easy.

## Maven Goals ##

### compile ###

This goal parses Java source files, extracts JAll snippets, and performs the necessary transformations, including writing files to their appropriate locations within the project.

### pom ###

The jall-maven-plugin itself does not manage compilation of all the various JVM languages. Instead, you can run the `pom` goal to print out a working POM file configuration for running the jall-maven-plugin itself, compiling JRuby, Clojure, etc., and finally compiling and optionally running a default main class.

