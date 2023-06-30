package com.github.sulir.jamabuild;

import com.github.sulir.jamabuild.building.BuildResult;
import com.github.sulir.jamabuild.building.Builder;
import com.github.sulir.jamabuild.building.BuilderFactory;
import com.github.sulir.jamabuild.filtering.Criterion;
import com.github.sulir.jamabuild.filtering.ProjectFilter;
import com.github.sulir.jamabuild.loading.Loader;
import com.github.sulir.jamabuild.loading.LoaderFactory;
import com.github.sulir.jamabuild.loading.ProjectLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Build {
    private static final Logger log = LoggerFactory.getLogger(Build.class);

    private final String type;
    private final String projectId;
    private final String rootDirectory;

    public Build(String type, String projectId, String rootDirectory) {
        this.type = type;
        this.projectId = projectId;
        this.rootDirectory = rootDirectory;
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.err.println("Arguments: <type> <project_id> [root_directory]");
                return;
            }

            String rootDirectory = args.length == 3 ? args[2] : System.getProperty("user.dir");
            new Build(args[0], args[1], rootDirectory).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Settings settings = Settings.load(rootDirectory);
            Project project = loadProject(settings);
            ProjectFilter projectFilter = ProjectFilter.load(settings);

            if (projectFilter.exclusionMatches(project, Criterion.Phase.PRE_BUILD))
                return;

            build(project);

            if (projectFilter.exclusionMatches(project, Criterion.Phase.POST_BUILD))
                return;

            log.info("Finished");
        } catch (ProjectLoadingException e) {
            log.error("Failed to load project: {}", e.getMessage());
        }
    }

    private Project loadProject(Settings settings) throws ProjectLoadingException {
        log.info("Loading");
        Loader loader = LoaderFactory.createLoader(rootDirectory, type, projectId);
        Project project = loader.load();
        project.setSettings(settings);
        return project;
    }

    private void build(Project project) {
        log.info("Building");
        Builder builder = BuilderFactory.createBuilder(project);

        BuildResult result = builder.runBuild();
        result.write(project.getResultFile());

        if (result.isSuccessful()) {
            log.info("Copying JARs");
            builder.copyJARs();

            log.info("Copying dependencies");
            builder.copyDependencies();
        }
    }
}
