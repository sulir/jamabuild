package com.sulir.github.jamabuild.loaders;

public class GitHubLoader extends GitLoader {
    private static final String URL = "https://github.com/%s.git";

    public GitHubLoader(String id) {
        super(String.format(URL, id));
    }
}
