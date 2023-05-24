package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

import java.util.ArrayList;
import java.util.List;

public class MavenBuilder extends Builder {
    public MavenBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "Maven";
    }

    @Override
    protected List<String> getBuildToolCommand() {
        List<String> command = new ArrayList<>(List.of("mvn", "-B", "clean", "package"));

        if (project.getSettings().skipTests())
            command.add("-DskipTests");

        return command;
    }
}
