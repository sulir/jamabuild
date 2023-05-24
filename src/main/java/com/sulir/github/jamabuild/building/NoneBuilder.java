package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

import java.util.List;

public class NoneBuilder extends Builder{
    public NoneBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "None";
    }

    @Override
    protected List<String> getBuildToolCommand() {
        return List.of("false");
    }
}
