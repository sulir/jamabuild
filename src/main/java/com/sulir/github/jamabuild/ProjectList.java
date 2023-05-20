package com.sulir.github.jamabuild;

import com.sulir.github.jamabuild.projects.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ProjectList extends ArrayList<Project> {
    private static final Logger log = LoggerFactory.getLogger(ProjectList.class);

    public void load() {
        for (Project project : this) {
            log.info("Downloading project {}", project.getId());
            project.load();
        }
    }
}
