package com.sulir.github.jamabuild.loaders;

public class LoaderFactory {
    public static Loader createLoader(String type, String id) {
        return switch (type) {
            case "local" -> new LocalLoader(id);
            case "git" -> new GitLoader(id);
            case "github" -> new GitHubLoader(id);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
