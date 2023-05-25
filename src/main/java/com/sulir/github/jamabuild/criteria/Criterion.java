package com.sulir.github.jamabuild.criteria;

import com.sulir.github.jamabuild.Project;

public interface Criterion {
    boolean isMet(Project project);
}
