package com.sulir.github.jamabuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sulir.github.jamabuild.building.BuildResult;
import com.sulir.github.jamabuild.building.Builder;
import com.sulir.github.jamabuild.building.BuilderFactory;
import com.sulir.github.jamabuild.loading.Loader;
import com.sulir.github.jamabuild.loading.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

public class Build {
    private static final Logger log = LoggerFactory.getLogger(Build.class);

    private final String type;
    private final String projectId;

    public Build(String type, String projectId) {
        this.type = type;
        this.projectId = projectId;
    }

    public static void main(String[] args) {
        try {
            new Build(args[0], args[1]).run(args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run(String settingsFileName) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Settings settings;
        try(InputStream settingsFile = new FileInputStream(settingsFileName)) {
            settings = mapper.readValue(settingsFile, Settings.class);
        } catch (Exception e) {
            settings = new Settings(null, null);
        }

        log.info("Loading");
        Loader loader = LoaderFactory.createLoader(type, projectId);
        Project project = loader.load();
        project.setSettings(settings);

        log.info("Building");
        Builder builder = BuilderFactory.createBuilder(project);
        BuildResult result = builder.runBuild();
        result.write(project.getResultFile());

        log.info("Finished");
    }
}
