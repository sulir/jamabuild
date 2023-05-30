package com.github.sulir.jamabuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public record Settings(
        String dockerImage,
        String timeout,
        Boolean skipTests,
        String[] preInclude,
        String[] preExclude,
        String[] postInclude,
        String[] postExclude
) {
    private static final String FILE_NAME = "jamabuild.yml";

    public static Settings load(String directory) {
        String path = Path.of(directory, FILE_NAME).toString();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Settings settings;

        try(InputStream settingsFile = new FileInputStream(path)) {
            settings = mapper.readValue(settingsFile, Settings.class);
        } catch (Exception e) {
            settings = new Settings();
        }
        return settings;
    }

    public Settings() {
        this(null, null, null, null, null, null, null);
    }

    @Override
    public String dockerImage() {
        return dockerImage == null ? "sulir/jamabuild:master": dockerImage;
    }

    @Override
    public String timeout() {
        return timeout == null ? "1h" : timeout;
    }

    @Override
    public Boolean skipTests() {
        return skipTests == null || skipTests;
    }

    @Override
    public String[] preInclude() {
        return preInclude == null ? new String[0] : preInclude;
    }

    @Override
    public String[] preExclude() {
        return preExclude == null ? new String[0] : preExclude;
    }

    @Override
    public String[] postInclude() {
        return postInclude == null ? new String[0] : postInclude;
    }

    @Override
    public String[] postExclude() {
        return postExclude == null ? new String[0] : postExclude;
    }
}
