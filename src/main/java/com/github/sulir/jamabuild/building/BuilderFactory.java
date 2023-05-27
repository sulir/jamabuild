package com.github.sulir.jamabuild.building;

import com.github.sulir.jamabuild.Project;

public class BuilderFactory {
    public static Builder createBuilder(Project project) {
        if (project.hasSourceFile("pom.xml"))
            return new MavenBuilder(project);
        else if (project.hasSourceFile("build.gradle{,.kts}"))
            return new GradleBuilder(project);
        else
            return new NoneBuilder(project);
    }
}
