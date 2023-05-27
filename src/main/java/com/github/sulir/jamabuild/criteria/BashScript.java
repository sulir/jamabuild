package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.exclusion.AllowedTypes;
import com.github.sulir.jamabuild.exclusion.Criterion;
import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.exclusion.CriterionType;

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
            Process process = Runtime.getRuntime().exec(command, null, project.getDirectory().toFile());
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
