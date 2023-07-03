package com.github.sulir.jamabuild.building;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class BuildResult {
    private static final Logger log = LoggerFactory.getLogger(Builder.class);

    private final String toolName;
    private final int exitCode;

    public BuildResult(String toolName, int exitValue) {
        this.toolName = toolName;
        this.exitCode = exitValue;
    }

    public boolean isSuccessful() {
        return exitCode == 0;
    }

    public void write(Path file) {
        try (PrintWriter writer = new PrintWriter(file.toFile())) {
            writer.println("tool\texit_code");
            writer.println(toolName + "\t" + exitCode);
        } catch (FileNotFoundException e) {
            log.error("Cannot write build result to {}: {}", file, e.getMessage());
        }
    }

    public static BuildResult read(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            // Skip the header line
            reader.readLine();

            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String toolName = parts[0];
                    int exitCode = Integer.parseInt(parts[1]);
                    return new BuildResult(toolName, exitCode);
                }
            }
        } catch (IOException e) {
            log.error("Cannot read build result: {}", e.getMessage());
        }
        return null;
    }
}
