package com.github.sulir.jamabuild.processes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessList {
    private static final Logger log = LoggerFactory.getLogger(ProcessList.class);

    private final List<DockerProcess> processes = new ArrayList<>();
    private final String rootDirectory;

    public ProcessList(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public void addProjects(Path projectFile) {
        try (Stream<String> lines = Files.lines(projectFile)) {
            lines.forEachOrdered(line -> {
                String[] records = line.split("\t");

                if (records.length == 2) {
                    String type = records[0];
                    String id = records[1];
                    processes.add(new DockerProcess(type, id, rootDirectory));
                } else {
                    log.warn("Invalid line in {}: {}", projectFile, line);
                    log.warn("Please see README for information on the project list format");
                }
            });
        } catch (NoSuchFileException e) {
            log.error("File {} not found", projectFile);
            log.error("Please create this file with a list of projects to build");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<DockerProcess> getProcesses() {
        return processes;
    }

    public void runAll(BuildingState state) {
        for (DockerProcess process : processes) {
            if (state.shouldSkipProject(process)) {
                log.info("Skipping project {} as it was built in previous session", process.getProjectId());
            } else {
                log.info("Project {}", process.getProjectId());
                process.run();
                state.didBuildProject(process);
            }
        }
        state.didBuildAllProjects();
    }
}
