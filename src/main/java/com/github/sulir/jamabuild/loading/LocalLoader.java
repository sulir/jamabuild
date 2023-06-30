package com.github.sulir.jamabuild.loading;

import com.github.sulir.jamabuild.Project;

import java.io.File;
import java.nio.file.Path;

public class LocalLoader extends Loader {
    public LocalLoader(String projectsDirectory, String projectId) {
        super(projectsDirectory, projectId);
    }

    @Override
    public Project load() throws ProjectLoadingException {
        String projectPath = Path.of(projectsDirectory, projectId).toString();

        if (new File(projectPath).isDirectory())
            return new Project(projectId, projectPath);
        else
            throw new ProjectLoadingException("Local project directory not found: " + projectId);
    }
}
