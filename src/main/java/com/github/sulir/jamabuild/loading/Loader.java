package com.github.sulir.jamabuild.loading;

import com.github.sulir.jamabuild.Project;

public abstract class Loader {
    protected final String projectsDirectory;
    protected final String projectId;

    public Loader(String projectsDirectory, String projectId) {
        this.projectsDirectory = projectsDirectory;
        this.projectId = projectId;
    }

    public abstract Project load() throws ProjectLoadingException;
}
