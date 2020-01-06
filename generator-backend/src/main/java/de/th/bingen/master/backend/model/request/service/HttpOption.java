package de.th.bingen.master.backend.model.request.service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class HttpOption {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    private String host;
    @Valid
    private List<HttpPath> paths;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<HttpPath> getPaths() {
        return paths;
    }

    public void setPaths(List<HttpPath> paths) {
        this.paths = paths;
    }
}
