package de.th.bingen.master.backend.model.response;

import de.th.bingen.master.backend.model.request.Port;
import de.th.bingen.master.backend.model.request.container.EnvironmentVariable;

import java.util.List;

public class ImageAnalysis {
    private String name;
    private List<EnvironmentVariable> environments;
    private List<Port> ports;
    private List<String> mountPaths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnvironmentVariable> getEnvironments() {
        return environments;
    }

    public void setEnvironments(List<EnvironmentVariable> environments) {
        this.environments = environments;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public List<String> getMountPaths() {
        return mountPaths;
    }

    public void setMountPaths(List<String> mountPaths) {
        this.mountPaths = mountPaths;
    }
}
