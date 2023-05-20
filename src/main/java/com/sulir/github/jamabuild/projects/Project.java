package com.sulir.github.jamabuild.projects;

import java.nio.file.Path;

public abstract class Project {
    private static final String PROJECTS_DIR = "projects";

    protected String id;
    protected String directory;

    public Project(String id) {
        this.id = id;
        this.directory = Path.of(PROJECTS_DIR, createValidFileName(id)).toString();
    }

    public String getId() {
        return id;
    }

    public abstract void load();

    private String createValidFileName(String id) {
        return id.replaceAll("\\W", "_");
    }
}
