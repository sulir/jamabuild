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

        List<String> jdepsCommand = new ArrayList<>(Arrays.asList("jdeps", "-R"));
        try (Stream<Path> paths = Files.walk(jarsDir).filter(Files::isRegularFile)) {
            jdepsCommand.addAll(paths.map(Path::toString).toList());

            ProcessBuilder pb = new ProcessBuilder(jdepsCommand);
            // using this temp file to prevent hanging on full output stream
            File outputFile = project.getDirectory().resolve("processOutput.txt").toFile();
            pb.redirectOutput(outputFile);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
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