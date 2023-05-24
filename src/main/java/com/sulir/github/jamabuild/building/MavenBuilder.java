package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

public class MavenBuilder extends Builder {
    public MavenBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "Maven";
    }

    @Override
    protected String[] getCommand() {
        return new String[] {"mvn", "clean", "package"};
    }
}
