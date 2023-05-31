package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;

@AllowedPhases(Criterion.Phase.POST_BUILD)
public class UnresolvedReferences extends Criterion {

    public UnresolvedReferences(Phase phase, Type type) {
        super(phase, type);
    }

    @Override
    public boolean isMet(Project project) {
        Path jarsDir = project.getJARsDir();

        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("jdeps", "-verbose", "-R", jarsDir.toString()));
            Process p = pb.start();
            p.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("not found")) {
                    return true;
                }
            }
            br.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}