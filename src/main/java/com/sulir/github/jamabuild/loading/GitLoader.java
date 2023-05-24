package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;
import org.eclipse.jgit.api.Git;

import java.nio.file.Path;

public class GitLoader extends Loader {
    public GitLoader(String projectId) {
        super(projectId);
    }

    @Override
    public Project load() throws Exception {
        return gitClone(projectId);
    }

    protected Project gitClone(String url) throws Exception {
        Project project = new Project(projectId, getDirectory());

        if (!project.getSource().toFile().exists()) {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(project.getSource().toFile())
                    .call();
        }

        return project;
    }

    private String getDirectory() {
        return Path.of(PROJECTS_DIR, projectId.replaceAll("\\W", "_")).toString();
    }
}
