package com.github.sulir.jamabuild.loading;

import org.eclipse.jgit.api.errors.GitAPIException;

public class ProjectLoadingException extends Exception {
    public ProjectLoadingException(GitAPIException e) {
        super(e);
    }

    public ProjectLoadingException(String s) {
        super(s);
    }
}
