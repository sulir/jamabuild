package com.sulir.github.jamabuild.loaders;

import com.sulir.github.jamabuild.Project;
import org.eclipse.jgit.api.Git;

import java.io.File;

public class GitLoader extends Loader {
    public GitLoader(String id) {
        super(id);
    }

    @Override
    public Project load() throws Exception {
        Git.cloneRepository()
                .setURI(projectId)
                .setDirectory(new File(getDirectory()))
                .call();
        return new Project(projectId, getDirectory());
    }
}
