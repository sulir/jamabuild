package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@AllowedPhases(Criterion.Phase.PRE_BUILD)
public class AndroidSource extends Criterion {
    private static final String CONTENT = "import android.";

    public AndroidSource(Phase phase, Type type) {
        super(phase, type);
    }

    @Override
    public boolean isMet(Project project) {
        try (Stream<Path> files = Files.walk(project.getSourceDir())) {
            return files.filter(path -> path.toString().endsWith(".java"))
                    .anyMatch(path -> {
                        try {
                            return FileUtils.readFileToString(path.toFile(), "UTF-8").contains(CONTENT);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
