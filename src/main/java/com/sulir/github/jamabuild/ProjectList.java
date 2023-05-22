package com.sulir.github.jamabuild;

import com.sulir.github.jamabuild.loaders.Loader;
import com.sulir.github.jamabuild.loaders.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProjectList {
    private static final Logger log = LoggerFactory.getLogger(ProjectList.class);

    private final List<Loader> loaders = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();

    public void addFrom(Path projectFile) {
        try (Stream<String> lines = Files.lines(projectFile)) {
            lines.forEachOrdered(line -> {
                String[] records = line.split("\t");
                String type = records[0];
                String id = records[1];
                loaders.add(LoaderFactory.createLoader(type, id));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        for (Loader loader : loaders) {
            log.info("Downloading project {}", loader.getProjectId());
            try {
                Project project = loader.load();
                projects.add(project);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
