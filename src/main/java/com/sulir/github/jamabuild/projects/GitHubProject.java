package com.sulir.github.jamabuild.projects;

public class GitHubProject extends GitProject {
    private static final String URL = "https://github.com/%s.git";

    public GitHubProject(String id) {
        super(String.format(URL, id));
    }
}
