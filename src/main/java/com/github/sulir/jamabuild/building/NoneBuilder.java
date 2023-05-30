package com.github.sulir.jamabuild.building;

import com.github.sulir.jamabuild.Project;

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

    @Override
    public void copyJARs() {

    }

    @Override
    public void copyDependencies() {

    }
}
