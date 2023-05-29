package com.github.sulir.jamabuild;

import java.io.IOException;

public class DockerProcess {
    public static final String IMAGE = "sulir/jamabuild:master";
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

    public void run() {
        if (System.getenv("DEBUG") == null) {
            try {
                executeDocker();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Build.main(new String[] {type, projectId, rootDirectory});
        }
    }

    private void executeDocker() throws IOException, InterruptedException {
        String volume = rootDirectory + ":" + CONTAINER_DIR;
        String[] command = new String[] {"docker", "run", "-iv", volume, IMAGE, type, projectId};

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = builder.start();
        process.waitFor();
    }
}
