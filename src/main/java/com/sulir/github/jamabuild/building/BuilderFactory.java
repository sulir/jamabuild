package com.sulir.github.jamabuild.building;

import com.sulir.github.jamabuild.Project;

public class BuilderFactory {
    public static Builder createBuilder(Project project) {
        if (project.hasFile("pom.xml"))
            return new MavenBuilder();
        else if (project.hasFile("build.gradle{,.kts}"))
            return new GradleBuilder();
        else
            return new NoneBuilder();
    }
}
