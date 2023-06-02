package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@AllowedPhases(Criterion.Phase.PRE_BUILD)
public class SourceFile extends Criterion {
    private final String globPattern;
    private final String contentToSearch;

    public SourceFile(Phase phase, Type type, String globPattern) {
        super(phase, type);
        this.globPattern = globPattern;
        this.contentToSearch = null;
    }

    public SourceFile(Phase phase, Type type, String globPattern, String contentToSearch) {
        super(phase, type);
        this.globPattern = globPattern;
        this.contentToSearch = contentToSearch;
    }

    @Override
    public boolean isMet(Project project) {
        try (Stream<Path> paths = Files.walk(project.getSourceDir())) {
            return paths.anyMatch(this::matchesGlobAndContent);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean matchesGlobAndContent(Path path) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/" + globPattern);
        if (matcher.matches(path)) {
            if (contentToSearch != null) {
                try {
                    return FileUtils.readFileToString(path.toFile(), "UTF-8").contains(contentToSearch);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
