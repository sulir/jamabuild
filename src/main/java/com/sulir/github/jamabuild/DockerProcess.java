package com.sulir.github.jamabuild;

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

    public void run(String rootDirectory) {
        Build.main(new String[] {rootDirectory, type, projectId});
    }
}
