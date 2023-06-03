package com.github.sulir.jamabuild.processes;

import com.github.sulir.jamabuild.Build;
import com.github.sulir.jamabuild.Settings;

public class DockerProcess {
    private static final String CONTAINER_DIR = "/opt/data";

    private final String type;
    private final String projectId;
    private final String rootDirectory;

    public DockerProcess(String type, String projectId, String rootDirectory) {
        this.type = type;
        this.projectId = projectId;
        this.rootDirectory = rootDirectory;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectType() {
        return type;
    }

    public void run() {
        if (System.getenv("JAMABUILD_NO_DOCKER") == null) {
            executeDocker();
        } else {
            Build.main(new String[] {type, projectId, rootDirectory});
        }
    }

    private void executeDocker() {
        String image = Settings.load(rootDirectory).dockerImage();
        String volume = rootDirectory + ":" + CONTAINER_DIR;
        String[] command = new String[] {"docker", "run", "-iv", volume, image, type, projectId};

        new ConsoleProcess(command).run();
    }
}
