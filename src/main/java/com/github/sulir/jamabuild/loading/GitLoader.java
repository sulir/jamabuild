package com.github.sulir.jamabuild.loading;

import com.github.sulir.jamabuild.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.nio.file.Path;

public class GitLoader extends Loader {
    public GitLoader(String projectsDirectory, String projectId) {
        super(projectsDirectory, projectId);
    }

    @Override
    public Project load() throws ProjectLoadingException {
        return gitClone(projectId);
    }

    protected Project gitClone(String url) throws ProjectLoadingException {
        Project project = new Project(projectId, getDirectory());

        if (!project.getSourceDir().toFile().exists()) {
            try {
                Git.cloneRepository()
                        .setURI(url)
                        .setDirectory(project.getSourceDir().toFile())
                        .call();
            } catch (GitAPIException e) {
                throw new ProjectLoadingException(e);
            }
        }

        return project;
    }

    private String getDirectory() {
        return Path.of(projectsDirectory, projectId.replaceAll("\\W", "_")).toString();
    }
}
