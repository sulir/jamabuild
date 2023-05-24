package com.sulir.github.jamabuild;

import com.sulir.github.jamabuild.Build;

public class DockerProcess {
    private final String type;
    private final String projectId;

    public DockerProcess(String type, String projectId) {
        this.type = type;
        this.projectId = projectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void run() {
        Build.main(new String[] {type, projectId});
    }
}
