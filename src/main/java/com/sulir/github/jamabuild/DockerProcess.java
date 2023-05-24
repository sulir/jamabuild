package com.sulir.github.jamabuild;

public class DockerProcess {
    private static final String SETTINGS_FILE = "jamabuild.yml";

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
        Build.main(new String[] {type, projectId, SETTINGS_FILE});
    }
}
