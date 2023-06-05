# JaMaBuild: Mass Building of Java Projects

JaMaBuild is a tool that takes a list of Java projects as an input and tries to build all of them using Maven or Gradle. It can be used for the creation of software engineering research corpora (e.g., JAR files for static analysis) or for research about build tools.

The tool supports various project sources (local, Git, GitHub), inclusion and exclusion criteria (e.g., Android projects), artifact collection methods (resulting JAR files, build logs), and other configuration options.

## Requirements

JamaBuild requires:
- Java Development Kit >= 17
- Docker

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
