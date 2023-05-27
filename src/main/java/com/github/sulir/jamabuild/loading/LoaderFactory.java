package com.github.sulir.jamabuild.loading;

import java.nio.file.Path;

public class LoaderFactory {
    protected static final String PROJECTS_DIR = "projects";

    public static Loader createLoader(String rootDirectory, String type, String id) {
        String projectsDirectory = Path.of(rootDirectory, PROJECTS_DIR).toString();

        return switch (type) {
            case "local" -> new LocalLoader(projectsDirectory, id);
            case "git" -> new GitLoader(projectsDirectory, id);
            case "github" -> new GitHubLoader(projectsDirectory, id);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
