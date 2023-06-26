package com.github.sulir.jamabuild;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.StandardCopyOption;
import java.util.List;
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

    public Path getSourceDir() {
        return directory.resolve("source");
    }

    public Path getLogFile() {
        return directory.resolve("build.log");
    }

    public Path getResultFile() {
        return directory.resolve("result.tsv");
    }

    public Path getJARsDir() {
        return directory.resolve("jars");
    }

    public Path getDependenciesDir() {
        return directory.resolve("deps");
    }

    public List<Path> getSourceFiles(String pattern) {
        PathMatcher matcher = getPathMatcher(pattern);

        return streamFileTree().filter(Files::isRegularFile)
                .filter(matcher::matches)
                .toList();
    }

    private PathMatcher getPathMatcher(String pattern) {
        String glob = "glob:" + getSourceDir().resolve(pattern);
        return getSourceDir().getFileSystem().getPathMatcher(glob);
    }

    private Stream<Path> streamFileTree() {
        try {
            return Files.walk(getSourceDir());
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    public boolean hasSourceFile(String pattern) {
        PathMatcher matcher = getPathMatcher(pattern);

        return streamFileTree().filter(Files::isRegularFile)
                .anyMatch(matcher::matches);
    }

    public void copySourceFiles(String pattern, Path targetDir) {
        for (Path file : getSourceFiles(pattern)) {
            try {
                Files.createDirectories(targetDir);
                Files.copy(file, targetDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete() {
        try {
            FileUtils.deleteDirectory(directory.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getJARsAndDependencies() throws IOException {
        Stream<Path> paths = Stream.empty();
        paths = appendJARsFromDir(getJARsDir(), paths);
        paths = appendJARsFromDir(getDependenciesDir(), paths);

        return paths.map(Path::toString).toList();
    }

    private Stream<Path> appendJARsFromDir(Path directory, Stream<Path> paths) throws IOException {
        if (Files.exists(directory)) {
            Stream<Path> jarsPaths = Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".jar"));
            return Stream.concat(paths, jarsPaths);
        } else {
            return paths;
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
