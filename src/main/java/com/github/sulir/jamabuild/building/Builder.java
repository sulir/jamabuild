package com.github.sulir.jamabuild.building;

import com.github.sulir.jamabuild.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public abstract class Builder {
    protected static final Logger log = LoggerFactory.getLogger(Builder.class);
    protected final Project project;

    public Builder(Project project) {
        this.project = project;
    }

    public BuildResult runBuild() {
        List<String> command = constructCommand();
        log.info("Command: {}", String.join(" ", command));
        ProcessBuilder builder = new ProcessBuilder(command);

        builder.directory(project.getSourceDir().toFile());
        builder.redirectOutput(project.getLogFile().toFile());
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();
            process.waitFor();
            return new BuildResult(getToolName(), process.exitValue());
        } catch (IOException | InterruptedException e) {
            log.error("Error while running build tool: {}", e.getMessage());
            return new BuildResult(getToolName(), -1);
        }
    }

    private List<String> constructCommand() {
        List<String> timeout = List.of("timeout", "-k1m", project.getSettings().timeout());
        return Stream.concat(timeout.stream(), getBuildToolCommand().stream())
                .toList();
    }

    protected abstract String getToolName();
    protected abstract List<String> getBuildToolCommand();

    public abstract void copyJARs();

    public abstract void copyDependencies();
}
