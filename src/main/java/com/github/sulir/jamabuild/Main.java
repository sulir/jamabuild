package com.github.sulir.jamabuild;

import com.github.sulir.jamabuild.processes.ConsoleProcess;
import com.github.sulir.jamabuild.processes.DockerProcess;
import com.github.sulir.jamabuild.processes.ProcessList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final String PROJECTS_FILE = "projects.tsv";

    public static void main(String[] args) {
        String rootDirectory = args.length > 0 ? args[0] : System.getProperty("user.dir");
        log.info("Starting JaMaBuild in {}", rootDirectory);

        updateDockerImage();

        ProcessList processList = new ProcessList(rootDirectory);
        processList.addProjects(Path.of(rootDirectory, PROJECTS_FILE));
        processList.runAll();
    }

    private static void updateDockerImage() {
        new ConsoleProcess(new String[] {"docker", "pull", DockerProcess.IMAGE}).run();
    }
}
