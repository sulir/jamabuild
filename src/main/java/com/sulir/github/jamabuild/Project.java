package com.sulir.github.jamabuild;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

public class Project {
    private final String id;
    private final Path root;

    public Project(String id, String directory) {
        this.id = id;
        this.root = Path.of(directory);
    }

    public String getId() {
        return id;
    }

    public Path getRoot() {
        return root;
    }

    public Path getSource() {
        return root.resolve("source");
    }

    public Path getLog() {
        return root.resolve("build.log");
    }

    public Path getResultFile() {
        return root.resolve("result.tsv");
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
}
