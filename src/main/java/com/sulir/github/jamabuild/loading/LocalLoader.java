package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;

import java.nio.file.Path;

public class LocalLoader extends Loader {
    public LocalLoader(String projectsDirectory, String projectId) {
        super(projectsDirectory, projectId);
    }

    @Override
    public Project load() {
        return new Project(projectId, Path.of(projectsDirectory, projectId).toString());
    }
}
