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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean isMet(Project project) {
        Path jarsDir = project.getJARsDir();
        Path depsDir = project.getDependenciesDir();

        String javaVersion = System.getProperty("java.version");
        String majorVersion = javaVersion.startsWith("1.") ? javaVersion.substring(2, 3) : javaVersion.split("\\.")[0];

        List<String> jdepsCommand = new ArrayList<>(Arrays.asList("jdeps", "-summary", "--ignore-missing-deps", "--multi-release", majorVersion, "-recursive", "--module-path", depsDir.toString()));
        try (Stream<Path> pathsJars = Files.walk(jarsDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().toLowerCase().endsWith(".jar"));
             Stream<Path> pathsDeps = Files.walk(depsDir)
                     .filter(Files::isRegularFile)
                     .filter(p -> p.toString().toLowerCase().endsWith(".jar"))) {
            List<String> allJarFiles = Stream.concat(pathsJars, pathsDeps)
                    .map(Path::toString)
                    .toList();

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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}