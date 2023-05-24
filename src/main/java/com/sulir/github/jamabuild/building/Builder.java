package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

import java.io.IOException;

public abstract class Builder {
    protected final Project project;

    public Builder(Project project) {
        this.project = project;
    }

    public BuildResult runBuild() {
        ProcessBuilder builder = new ProcessBuilder(getCommand());
        builder.directory(project.getSource().toFile());
        builder.redirectOutput(project.getLog().toFile());
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            process.waitFor();
            return new BuildResult(getToolName(), process.exitValue());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new BuildResult(getToolName(), -1);
        }
    }

    protected abstract String getToolName();
    protected abstract String[] getCommand();
}
