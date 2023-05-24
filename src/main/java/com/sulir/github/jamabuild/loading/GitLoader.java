package com.sulir.github.jamabuild.loading;

import com.sulir.github.jamabuild.Project;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.nio.file.Path;

public class GitLoader extends Loader {
    public GitLoader(String projectId) {
        super(projectId);
    }

    public String getDirectory() {
        return Path.of(PROJECTS_DIR, createValidFileName(projectId)).toString();
    }

    private String createValidFileName(String id) {
        return id.replaceAll("\\W", "_");
    }

    @Override
    public Project load() throws Exception {
        return gitClone(projectId);
    }

    protected Project gitClone(String url) throws Exception {
        if (!new File(getDirectory()).exists()) {
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(getDirectory()))
                    .call();
        }
        return new Project(projectId, getDirectory());
    }
}
