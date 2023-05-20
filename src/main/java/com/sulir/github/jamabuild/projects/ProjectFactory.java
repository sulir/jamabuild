package com.sulir.github.jamabuild.projects;

public class ProjectFactory {
    public static Project createProject(String type, String id) {
        return switch (type) {
            case "local" -> new LocalProject(id);
            case "git" -> new GitProject(id);
            case "github" -> new GitHubProject(id);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
