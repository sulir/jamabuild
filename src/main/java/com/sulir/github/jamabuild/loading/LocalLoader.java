package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;

import java.nio.file.Path;

public class LocalLoader extends Loader {
    public LocalLoader(String projectId) {
        super(projectId);
    }

    @Override
    public String getDirectory() {
        return Path.of(PROJECTS_DIR, projectId).toString();
    }

    @Override
    public Project load() {
        return new Project(projectId, getDirectory());
    }
}
