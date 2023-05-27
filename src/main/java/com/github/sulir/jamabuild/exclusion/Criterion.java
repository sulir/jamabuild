package com.github.sulir.jamabuild.exclusion;

import com.github.sulir.jamabuild.Project;

public interface Criterion {
    boolean isMet(Project project);
}
