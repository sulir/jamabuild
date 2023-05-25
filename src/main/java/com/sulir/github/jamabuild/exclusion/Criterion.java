package com.sulir.github.jamabuild.exclusion;

import com.sulir.github.jamabuild.Project;

public interface Criterion {
    boolean isMet(Project project);
}
