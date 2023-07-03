package com.github.sulir.jamabuild.building;

import com.github.sulir.jamabuild.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GradleBuilder extends Builder {
    private static final String CACHE_DIR = System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1";

    public GradleBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "Gradle";
    }

    @Override
    protected List<String> getBuildToolCommand() {
        String executable = project.hasSourceFile("gradlew") ? "./gradlew" : "gradle";
        List<String> command = new ArrayList<>(List.of(executable, "clean"));

        if (project.getSettings().skipTests())
            command.add("assemble");
        else
            command.add("build");

        command.add("--console=plain");

        return command;
    }

    @Override
    public void copyJARs() {
        project.copySourceFiles("{**/,}build/*.jar", project.getJARsDir());
    }

    @Override
    public void copyDependencies() {
        String cache = CACHE_DIR + "/**/*.jar";
        PathMatcher matcher = Path.of(cache).getFileSystem().getPathMatcher("glob:" + cache);

        try (Stream<Path> files = Files.walk(Path.of(CACHE_DIR))) {
            files.filter(Files::isRegularFile).filter(matcher::matches).forEachOrdered(dependency -> {
                try {
                    Files.createDirectories(project.getDependenciesDir());
                    Path target = project.getDependenciesDir().resolve(dependency.getFileName());
                    Files.copy(dependency, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    log.error("Error while copying dependencies: {}", e.getMessage());
                }
            });
        } catch (IOException e) {
            log.error("Error reading Gradle cache directory: {}", e.getMessage());
        }
    }
}
