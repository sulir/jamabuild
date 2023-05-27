package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;

public abstract class Loader {
    protected final String projectsDirectory;
    protected final String projectId;

    public Loader(String projectsDirectory, String projectId) {
        this.projectsDirectory = projectsDirectory;
        this.projectId = projectId;
    }

    public abstract Project load() throws Exception;
}
