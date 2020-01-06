package de.th.bingen.master.backend.configuration;

import de.th.bingen.master.backend.model.request.container.EnvironmentVariable;

import java.util.List;

public class DatabaseConfiguration {
    private String name;
    private String image;
    private int port;
    private List<String> mustSetVariables;
    private String mountPath;
    private List<EnvironmentVariable> environmentVariables;

    public List<String> getMustSetVariables() {
        return mustSetVariables;
    }

    public void setMustSetVariables(List<String> mustSetVariables) {
        this.mustSetVariables = mustSetVariables;
    }

    public List<EnvironmentVariable> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(List<EnvironmentVariable> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
