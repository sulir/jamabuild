package com.github.sulir.jamabuild.filtering;

import com.github.sulir.jamabuild.Project;

public abstract class Criterion {
    public enum Phase {
        PRE_BUILD, POST_BUILD
    }

    public enum Type {
        INCLUDE, EXCLUDE
    }

    private final Phase phase;
    private final Type type;

    public Criterion(Phase phase, Type type) {
        this.phase = phase;
        this.type = type;
    }

    public abstract boolean isMet(Project project);

    public Phase getPhase() {
        return phase;
    }

    public Type getType() {
        return type;
    }
}
