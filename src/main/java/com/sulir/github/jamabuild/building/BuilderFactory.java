package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

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
