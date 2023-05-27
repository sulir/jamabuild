package com.sulir.github.jamabuild;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

public class Project {
    private final String id;
    private final Path directory;
    private Settings settings;

    public Project(String id, String directory) {
        this.id = id;
        this.directory = Path.of(directory);
    }

    public Path getDirectory() {
        return directory;
    }

    public Path getSource() {
        return directory.resolve("source");
    }

    public Path getLog() {
        return directory.resolve("build.log");
    }

    public Path getResultFile() {
        return directory.resolve("result.tsv");
    }

    public boolean hasSourceFile(String pattern) {
        String glob = "glob:" + getSource().resolve(pattern);
        PathMatcher matcher = getSource().getFileSystem().getPathMatcher(glob);

        try (Stream<Path> files = Files.walk(getSource())) {
            return files.anyMatch(matcher::matches);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete() {
        try {
            FileUtils.deleteDirectory(directory.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String toString() {
        return id;
    }
}
