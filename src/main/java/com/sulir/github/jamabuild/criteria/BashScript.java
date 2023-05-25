package com.sulir.github.jamabuild.criteria;

import com.sulir.github.jamabuild.Project;

import java.io.IOException;

@AllowedTypes({CriterionType.PRE_BUILD, CriterionType.POST_BUILD})
public class BashScript implements Criterion {
    private final String script;

    public BashScript(String script) {
        this.script = script;
    }

    @Override
    public boolean isMet(Project project) {
        String[] command = new String[] {"bash", "-c", script};

        try {
            Process process = Runtime.getRuntime().exec(command, null, project.getRoot().toFile());
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
