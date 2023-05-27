package com.github.sulir.jamabuild.building;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;

public class BuildResult {
    private final String toolName;
    private final int exitCode;

    public BuildResult(String toolName, int exitValue) {
        this.toolName = toolName;
        this.exitCode = exitValue;
    }

    public void write(Path file) {
        try (PrintWriter writer = new PrintWriter(file.toFile())) {
            writer.println("tool\texit_code");
            writer.println(toolName + "\t" + exitCode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
