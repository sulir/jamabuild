package com.sulir.github.jamabuild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final String PROJECTS_FILE = "projects.tsv";

    public static void main(String[] args) {
        log.info("Starting JaMaBuild");
        ProcessList processList = new ProcessList();
        processList.addProjects(Path.of(PROJECTS_FILE));
        processList.runAll();
    }
}
