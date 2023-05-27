package com.github.sulir.jamabuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.sulir.jamabuild.exclusion.CriteriaTester;
import com.github.sulir.jamabuild.loading.Loader;
import com.github.sulir.jamabuild.building.BuildResult;
import com.github.sulir.jamabuild.building.Builder;
import com.github.sulir.jamabuild.building.BuilderFactory;
import com.github.sulir.jamabuild.exclusion.Criterion;
import com.github.sulir.jamabuild.exclusion.CriterionFactory;
import com.github.sulir.jamabuild.exclusion.CriterionType;
import com.github.sulir.jamabuild.loading.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Build {
    private static final Logger log = LoggerFactory.getLogger(Build.class);
    private static final String SETTINGS_FILE = "jamabuild.yml";

    private final String rootDirectory;
    private final String type;
    private final String projectId;

    public Build(String rootDirectory, String type, String projectId) {
        this.rootDirectory = rootDirectory;
        this.type = type;
        this.projectId = projectId;
    }

    public static void main(String[] args) {
        try {
            new Build(args[0], args[1], args[2]).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        Settings settings = loadSettings(Path.of(rootDirectory, SETTINGS_FILE).toString());
        Project project = loadProject(settings);

        List<Criterion> preExclude = loadExclude(settings, CriterionType.PRE_BUILD);
        if (new CriteriaTester(preExclude, project).anyMatches())
            return;

        build(project);

        List<Criterion> postExclude = loadExclude(settings, CriterionType.POST_BUILD);
        if (new CriteriaTester(postExclude, project).anyMatches())
            return;

        log.info("Finished");
    }

    private void build(Project project) {
        log.info("Building");
        Builder builder = BuilderFactory.createBuilder(project);
        BuildResult result = builder.runBuild();
        result.write(project.getResultFile());
    }

    private Project loadProject(Settings settings) throws Exception {
        log.info("Loading");
        Loader loader = LoaderFactory.createLoader(rootDirectory, type, projectId);
        Project project = loader.load();
        project.setSettings(settings);
        return project;
    }

    private Settings loadSettings(String settingsFileName) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Settings settings;
        try(InputStream settingsFile = new FileInputStream(settingsFileName)) {
            settings = mapper.readValue(settingsFile, Settings.class);
        } catch (Exception e) {
            settings = new Settings(null, null, null, null);
        }
        return settings;
    }

    private List<Criterion> loadExclude(Settings settings, CriterionType type) {
        CriterionFactory exclude = new CriterionFactory(type);
        List<Criterion> list = new ArrayList<>();
        String[] criteria = type == CriterionType.PRE_BUILD ? settings.preExclude() : settings.postExclude();

        for (String criterion : criteria) {
            try {
                list.add(exclude.createCriterion(criterion));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
