package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.building.BuildResult;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;

@AllowedPhases(Criterion.Phase.POST_BUILD)
public class FailedBuild extends Criterion {

    public FailedBuild(Phase phase, Type type) {
        super(phase, type);
    }

    @Override
    public boolean isMet(Project project, BuildResult buildResult) {
        return buildResult != null && !buildResult.isSuccessful();
    }
}
