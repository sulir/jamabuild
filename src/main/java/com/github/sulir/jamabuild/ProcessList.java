package com.github.sulir.jamabuild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessList {
    private static final Logger log = LoggerFactory.getLogger(ProcessList.class);

    private final List<DockerProcess> processes = new ArrayList<>();

    public void addProjects(Path projectFile) {
        try (Stream<String> lines = Files.lines(projectFile)) {
            lines.forEachOrdered(line -> {
                String[] records = line.split("\t");
                String type = records[0];
                String id = records[1];
                processes.add(new DockerProcess(type, id));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runAll(String rootDirectory) {
        for (DockerProcess process : processes) {
            log.info("Project {}", process.getProjectId());
            process.run(rootDirectory);
        }
    }
}
