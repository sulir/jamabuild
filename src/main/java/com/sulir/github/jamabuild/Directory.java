package com.sulir.github.jamabuild;

import java.nio.file.Path;

public class Directory {
    public static final String PROJECTS_FILE = "projects.tsv";

    public ProjectList loadProjects() {
        ProjectList projectList = new ProjectList();
        projectList.addFrom(Path.of(PROJECTS_FILE));
        return projectList;
    }
}
