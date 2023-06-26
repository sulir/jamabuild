# JaMaBuild: Mass Building of Java Projects

JaMaBuild is a tool that takes a list of Java projects as an input and tries to build all of them using Maven or Gradle. It can be used for the creation of software engineering research corpora (e.g., JAR files for static analysis) or for research about build tools.

The tool supports various project sources (local, Git, GitHub), inclusion and exclusion criteria (e.g., Android projects), artifact collection methods (resulting JAR files, build logs), and other configuration options.

## Requirements

JamaBuild requires:
- Java Development Kit >= 17
- Docker (preferably in [rootless mode](https://docs.docker.com/engine/security/rootless/) if on Linux)

You can build the project from source using `mvn package` or [download a pre-built JAR snapshot](https://github.com/sulir/jamabuild/releases/download/snapshot/jamabuild.jar).

## Basic Usage

First, create an empty directory - we will call it the root data directory. In it, create the main input file, `projects.tsv`, with a list of projects to build. Here is a short example of its contents:
<pre>
github&#9;apache/commons-lang
git&#9;https://github.com/sulir/iostudy.git
</pre>
Note the fields in each line (the project type and ID/URL) are separated by tabs, so make sure your editor is set correctly. You can also download the [example](https://github.com/sulir/jamabuild/blob/master/.github/files/projects.tsv?raw=true).

To start the building process, run:
```shell
java -jar jamabuild.jar $root_dir
```

The optional parameter `$root_dir` represents the root data path. If defaults to the current working directory.

## Project List

The file `projects.tsv` consists of a list of projects to build, one per line. Each line consists of the project type and project ID, separated by a tab. The currently supported project types are:
- `local`: the project's source code is already present in `projects/$project_id/source`.
- `git`: the project is cloned from the given Git URL.
- `github`: the project is cloned from GitHub.

## Configuration File

The optional YAML configuration file `jamabuild.yml` contains detailed settings of the specific aspects of the building process. Here is a specific example that sets the build timeout to 2 hours and excludes projects importing the Android API:
```yaml
timeout: 2h
preExclude:
  - AndroidSource
```

The currently supported options are:
- `dockerImage`: the name of the Docker image to run for each project, instead of [the default one](https://hub.docker.com/r/sulir/jamabuild).
- `timeout`: the time when the build process is stopped to prevent stalling; the default is `1h`.
- `skipTests`: whether to skip running tests during the build. The default is `true`.
- `preInclude`, `preExclude` - lists of criteria to test on the project before it is built. If at least one criterion from `preInclude` is not met or at least one `preExclude` criterion is met, the project is excluded and deleted.
- `postInclude`, `postExclude` - similar to the previous options but executed after each project's build process.

## Filtering Criteria

Each inclusion and exclusion criterion in the configuration file consists of the criterion name and an optional parameter, separated by space. The currently supported pre-build filtering criteria are:
- `AndroidSource`: searches for the import of Android API in Java source files.
- `BashScript script_text`: executes the given Bash script. The criterion is met if its exit value is zero.
- `SourceFile pattern text`: the criterion is met if at least one file matching the given glob pattern is present in the source tree of the project. If specified, the given text must be also contained in at least one matched file.

Here is a list of post-build criteria:
- `BashScript script_text`: exactly the same as the pre-build criterion but executed after the build.
- `FailedBuild`: checks whether the build failed. If so, this criterion is met.
- `UnresolvedReferences`: searches for unresolved references inside the project and dependency JARs using the `jdeps` tool. Ths criterion is met if at least one such reference was found.

Custom criteria can be implemented by extending the class [Criterion](src/main/java/com/github/sulir/jamabuild/filtering/Criterion.java).

## Directory Structure

JaMaBuild follows the "convention over configuration" principle. The root data path has the following structure:

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

The directory name for each project is automatically derived from its ID/URL. The `deps` directory contains dependency JARs, and `jars` contains the output artifacts (JARs) of the project itself. The files `build.log` represents a complete textual log of the build process. The file `results.tsv` contains the name of the used build tool and the exit code, e.g.:
<pre>
tool&#9;exit_code
Maven&#9;0
</pre>

A screencast demonstrating the tool usage will be added in near future.
