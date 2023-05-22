package com.sulir.github.jamabuild.loaders;

import com.sulir.github.jamabuild.Project;

public class LocalLoader extends Loader {
    public LocalLoader(String projectId) {
        super(projectId);
    }

    @Override
    public Project load() {
        return new Project(projectId, getDirectory());
    }
}
