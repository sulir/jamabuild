package com.sulir.github.jamabuild.projects;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class GitProject extends Project {
    public GitProject(String id) {
        super(id);
    }

    @Override
    public void load() {
        try {
            Git.cloneRepository()
                    .setURI(id)
                    .setDirectory(new File(directory))
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
}
