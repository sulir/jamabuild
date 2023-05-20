package com.sulir.github.jamabuild;

import com.sulir.github.jamabuild.projects.ProjectFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Directory {
    public static final String PROJECTS_FILE = "projects.tsv";

    public ProjectList getProjectList() {
        ProjectList projectList = new ProjectList();

        try (Stream<String> lines = Files.lines(Path.of(PROJECTS_FILE))) {
            lines.forEachOrdered(line -> {
                String[] records = line.split("\t");
                String type = records[0];
                String id = records[1];
                projectList.add(ProjectFactory.createProject(type, id));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return projectList;
    }
}
