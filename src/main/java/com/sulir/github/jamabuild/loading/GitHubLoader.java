package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;

public class GitHubLoader extends GitLoader {
    private static final String URL = "https://github.com/%s.git";

    public GitHubLoader(String projectsDirectory, String projectId) {
        super(projectsDirectory, projectId);
    }

    @Override
    public Project load() throws Exception {
        return gitClone(String.format(URL, projectId));
    }
}
