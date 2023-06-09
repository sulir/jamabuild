package com.github.sulir.jamabuild.processes;

import com.github.sulir.jamabuild.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BuildingState {

    private static final String FILE_NAME = ".state";

    private final String rootDirectory;

    private final List<String> alreadyBuiltProjectsLines;

    public BuildingState(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.alreadyBuiltProjectsLines = new ArrayList<>();
    }

    public BuildingState(String rootDirectory, List<String> processedProjectsLines) {
        this.rootDirectory = rootDirectory;
        this.alreadyBuiltProjectsLines = new ArrayList<>(processedProjectsLines);
    }

    public boolean shouldSkipProject(DockerProcess process) {
        String processLine = process.getProjectType() + "\t" + process.getProjectId();
        if (!alreadyBuiltProjectsLines.isEmpty() && alreadyBuiltProjectsLines.get(0).equals(processLine)) {
            alreadyBuiltProjectsLines.remove(0);
            return true;
        } else {
            return false;
        }
    }

    public void didBuildProject(DockerProcess process) {
        String processLine = process.getProjectType() + "\t" + process.getProjectId();
        Path stateFile = Path.of(rootDirectory, FILE_NAME);
        try {
            Files.write(stateFile, List.of(processLine), StandardOpenOption.APPEND);
        } catch (IOException e) {
            // should we report this?
        }
    }

    public void didBuildAllProjects() {
        Path stateFile = Path.of(rootDirectory, FILE_NAME);
        try {
            Files.delete(stateFile);
        } catch (IOException ex) {
            // ignore
        }
    }

    public static BuildingState getBuildingStateFor(String rootDirectory,
                                                    ProcessList currentProcessList,
                                                    Settings settings) {
        int settingsHash = settings.hashCode();

        Path stateFile = Path.of(rootDirectory, FILE_NAME);

        try {
            List<String> stateLines = Files.readAllLines(stateFile);
            if (stateLines.size() > 1 // state file contains at least a single already built project
                    && String.valueOf(settingsHash).equals(stateLines.get(0))) { // settings has to have the same hash
                List<String> processedProjectsLines = stateLines.subList(1, stateLines.size());
                if (isStateFileCompatibleWithCurrentProjectsConfiguration(processedProjectsLines, currentProcessList)) {
                    return new BuildingState(rootDirectory, processedProjectsLines);
                } else {
                    return prepareEmptyStateFor(rootDirectory, settings);
                }
            } else {
                return prepareEmptyStateFor(rootDirectory, settings);
            }
        } catch (IOException | SecurityException e) {
            // assume broken or missing state, restart from scratch
            return prepareEmptyStateFor(rootDirectory, settings);
        }
    }

    private static BuildingState prepareEmptyStateFor(String rootDirectory, Settings settings) {
        int settingsHash = settings.hashCode();
        Path stateFile = Path.of(rootDirectory, FILE_NAME);
        try {
            Files.write(stateFile, List.of(String.valueOf(settingsHash)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new BuildingState(rootDirectory);
    }

    private static boolean isStateFileCompatibleWithCurrentProjectsConfiguration(List<String> processedProjectsLines,
                                                                                 ProcessList currentProcessList) {
        List<DockerProcess> currentProcesses = currentProcessList.getProcesses();
        if (processedProjectsLines.isEmpty() || processedProjectsLines.size() >= currentProcesses.size()) {
            return false;
        }
        // State file has to be a prefix of the projects configuration
        for (int i = 0; i < processedProjectsLines.size(); i++) {
            String[] processedProject = processedProjectsLines.get(i).split("\t");
            String type = processedProject[0];
            String id = processedProject[1];
            DockerProcess ithProcess = currentProcesses.get(i);
            if (!type.equals(ithProcess.getProjectType()) || !id.equals(ithProcess.getProjectId())) {
                return false;
            }
        }
        return true;
    }
}
