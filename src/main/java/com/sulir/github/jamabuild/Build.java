package com.sulir.github.jamabuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sulir.github.jamabuild.building.BuildResult;
import com.sulir.github.jamabuild.building.Builder;
import com.sulir.github.jamabuild.building.BuilderFactory;
import com.sulir.github.jamabuild.criteria.CriteriaTester;
import com.sulir.github.jamabuild.criteria.Criterion;
import com.sulir.github.jamabuild.criteria.CriterionFactory;
import com.sulir.github.jamabuild.criteria.CriterionType;
import com.sulir.github.jamabuild.loading.Loader;
import com.sulir.github.jamabuild.loading.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        Settings settings = loadSettings(settingsFileName);
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
        Loader loader = LoaderFactory.createLoader(type, projectId);
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
