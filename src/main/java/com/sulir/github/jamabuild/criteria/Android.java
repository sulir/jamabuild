package com.sulir.github.jamabuild.criteria;

import com.sulir.github.jamabuild.Project;
import com.sulir.github.jamabuild.exclusion.AllowedTypes;
import com.sulir.github.jamabuild.exclusion.Criterion;
import com.sulir.github.jamabuild.exclusion.CriterionType;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@AllowedTypes(CriterionType.PRE_BUILD)
public class Android implements Criterion {
    private static final String CONTENT = "import android.";

    @Override
    public boolean isMet(Project project) {
        try (Stream<Path> files = Files.walk(project.getSource())) {
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
