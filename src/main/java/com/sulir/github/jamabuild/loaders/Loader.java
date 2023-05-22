package com.sulir.github.jamabuild.loaders;

import com.sulir.github.jamabuild.Project;

import java.nio.file.Path;

public abstract class Loader {
    private static final String PROJECTS_DIR = "projects";

    protected final String projectId;

    public Loader(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    protected String getDirectory() {
        return Path.of(PROJECTS_DIR, createValidFileName(projectId)).toString();
    }

    public abstract Project load() throws Exception;

    private String createValidFileName(String id) {
        return id.replaceAll("\\W", "_");
    }
}
