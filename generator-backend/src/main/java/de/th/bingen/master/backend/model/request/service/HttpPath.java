package de.th.bingen.master.backend.model.request.service;

import javax.validation.constraints.NotEmpty;

public class HttpPath {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    private String container;
    private String path;
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
