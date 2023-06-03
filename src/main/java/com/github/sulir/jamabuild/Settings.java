package com.github.sulir.jamabuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equals(dockerImage, settings.dockerImage) && Objects.equals(timeout, settings.timeout) && Objects.equals(skipTests, settings.skipTests) && Arrays.equals(preInclude, settings.preInclude) && Arrays.equals(preExclude, settings.preExclude) && Arrays.equals(postInclude, settings.postInclude) && Arrays.equals(postExclude, settings.postExclude);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dockerImage, timeout, skipTests);
        result = 31 * result + Arrays.hashCode(preInclude);
        result = 31 * result + Arrays.hashCode(preExclude);
        result = 31 * result + Arrays.hashCode(postInclude);
        result = 31 * result + Arrays.hashCode(postExclude);
        return result;
    }
}
