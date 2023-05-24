package com.sulir.github.jamabuild;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

public class Project {
    private final String id;
    private final Path directory;

    public Project(String id, String directory) {
        this.id = id;
        this.directory = Path.of(directory);
    }

    public String getId() {
        return id;
    }

    public boolean hasFile(String pattern) {
        String glob = "glob:" + directory.resolve(pattern);
        PathMatcher matcher = directory.getFileSystem().getPathMatcher(glob);

        try (Stream<Path> files = Files.walk(directory)) {
            return files.anyMatch(matcher::matches);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
