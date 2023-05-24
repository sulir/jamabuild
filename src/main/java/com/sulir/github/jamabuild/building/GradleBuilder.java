package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

public class GradleBuilder extends Builder {
    public GradleBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "Gradle";
    }

    @Override
    protected String[] getCommand() {
        return new String[] {"gradle", "clean", "assemble"};
    }
}
