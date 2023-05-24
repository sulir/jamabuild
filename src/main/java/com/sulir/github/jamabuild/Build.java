package com.sulir.github.jamabuild;

import com.sulir.github.jamabuild.building.Builder;
import com.sulir.github.jamabuild.building.BuilderFactory;
import com.sulir.github.jamabuild.loading.Loader;
import com.sulir.github.jamabuild.loading.LoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Build {
    private static final Logger log = LoggerFactory.getLogger(Build.class);

    public static void main(String[] args) {
        String type = args[0];
        String projectId = args[1];

        try {
            log.info("Loading");
            Loader loader = LoaderFactory.createLoader(type, projectId);
            Project project = loader.load();

            log.info("Building");
            Builder builder = BuilderFactory.createBuilder(project);
            builder.runBuild();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
