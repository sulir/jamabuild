package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

public class NoneBuilder extends Builder{
    public NoneBuilder(Project project) {
        super(project);
    }

    @Override
    protected String getToolName() {
        return "None";
    }

    @Override
    protected String[] getCommand() {
        return new String[] {"false"};
    }
}
