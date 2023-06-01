# JaMaBuild: Mass Building of Java Projects

JaMaBuild is a tool that takes a list of Java projects as an input and tries to build all of them using Maven or Gradle. It can be used for the creation of software engineering research corpora (e.g., JAR files for static analysis) or for research about build tools.

The tool supports various project sources (local, Git, GitHub), inclusion and exclusion criteria (e.g., Android projects), artifact collection methods (resulting JAR files, build logs), and other configuration options.

## Usage

To start the building process, run:
```shell
java -jar jamabuild.jar [root_dir]
```

The optional parameter represents the root data path. If defaults to the current working directory.

## Directory Structure

The root data path has the following structure:

```
projects.tsv
jamabuild.yml
projects/
    project_name/
        deps/
            *.jar
        jars/
            *.jar
        source/
        build.log
        results.tsv
    other_project/
        ...
```

More details will be added during the next few days.
