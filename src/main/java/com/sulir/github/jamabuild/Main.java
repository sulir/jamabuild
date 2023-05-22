package com.sulir.github.jamabuild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Directory directory = new Directory();

        log.info("Loading projects");
        ProjectList projectList = directory.loadProjects();
        projectList.loadAll();
    }
}
