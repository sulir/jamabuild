package com.github.sulir.jamabuild.building;

import com.github.sulir.jamabuild.Project;

import java.util.ArrayList;
import java.util.List;

public class GradleBuilder extends Builder {
    public GradleBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "Gradle";
    }

    @Override
    protected List<String> getBuildToolCommand() {
        String executable = project.hasSourceFile("gradlew") ? "./gradlew" : "gradle";
        List<String> command = new ArrayList<>(List.of(executable, "clean"));

        if (project.getSettings().skipTests())
            command.add("assemble");
        else
            command.add("build");

        command.add("--console=plain");

        return command;
    }
}
