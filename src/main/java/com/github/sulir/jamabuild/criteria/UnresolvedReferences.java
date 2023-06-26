package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@AllowedPhases(Criterion.Phase.POST_BUILD)
public class UnresolvedReferences extends Criterion {

    public UnresolvedReferences(Phase phase, Type type) {
        super(phase, type);
    }

    @Override
    public boolean isMet(Project project) {
        Path jarsDir = project.getJARsDir();
        Path dependenciesDir = project.getDependenciesDir();

        try (Stream<Path> jarsPaths = prepareJarsPathsFrom(jarsDir, dependenciesDir)) {
            List<String> allJarFiles = jarsPaths
                    .map(Path::toString)
                    .toList();

            return hasUnresolvedReferencesInJARs(allJarFiles, dependenciesDir, project);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Stream<Path> prepareJarsPathsFrom(Path jarsDir, Path dependenciesDir) throws IOException {
        Stream<Path> paths = Stream.empty();
        if (Files.exists(jarsDir)) {
            Stream<Path> jarsPaths = Files.walk(jarsDir)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".jar"));
            paths = Stream.concat(paths, jarsPaths);
        }
        if (Files.exists(dependenciesDir)) {
            Stream<Path> dependenciesPaths = Files.walk(dependenciesDir)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".jar"));
            paths = Stream.concat(paths, dependenciesPaths);
        }
        return paths;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean hasUnresolvedReferencesInJARs(List<String> allJarFiles,
                                                  Path dependenciesDir,
                                                  Project project) throws IOException, InterruptedException {
        if (allJarFiles.isEmpty()) {
            return false;
        }

        String javaVersion = System.getProperty("java.version");
        String majorVersion = javaVersion.startsWith("1.")
                ? javaVersion.substring(2, 3)
                : javaVersion.split("\\.")[0];

        List<String> jdepsCommand = new ArrayList<>(Arrays.asList("jdeps", "-summary", "--ignore-missing-deps",
                "--multi-release", majorVersion, "-recursive"));
        if (Files.exists(dependenciesDir)) {
            jdepsCommand.addAll(List.of("--module-path", dependenciesDir.toString()));
        }
        jdepsCommand.addAll(allJarFiles);

        ProcessBuilder pb = new ProcessBuilder(jdepsCommand);
        // using this temp file to prevent hanging on full output stream
        File outputFile = project.getDirectory().resolve("processOutput.txt").toFile();
        pb.redirectOutput(outputFile);
        pb.redirectError(outputFile);
        Process p = pb.start();
        p.waitFor();

        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("not found")) {
                reader.close();
                outputFile.delete();
                return true;
            }
        }

        reader.close();
        outputFile.delete();
        return false;
    }
}