package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;

public abstract class Loader {
    protected static final String PROJECTS_DIR = "projects";

    protected final String projectId;

    public Loader(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public abstract String getDirectory();

    public abstract Project load() throws Exception;
}
