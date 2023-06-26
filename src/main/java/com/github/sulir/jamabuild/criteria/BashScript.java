package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;

import java.io.IOException;

@AllowedPhases({Criterion.Phase.PRE_BUILD, Criterion.Phase.POST_BUILD})
public class BashScript extends Criterion {
    private final String script;

    public BashScript(Phase phase, Type type, String script) {
        super(phase, type);
        this.script = script;
    }

    @Override
    public boolean isMet(Project project) {
        String[] command = new String[] {"bash", "-c", script};

        try {
            Process process = Runtime.getRuntime().exec(command, null, project.getDirectory().toFile());
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
