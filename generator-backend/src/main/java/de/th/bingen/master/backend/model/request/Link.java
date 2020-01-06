package de.th.bingen.master.backend.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Link {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    private String name;
    @NotEmpty
    private String containerFrom;
    @NotEmpty
    private String containerTo;
    private String scheme;

    public Link() {
    }

    public Link(@NotEmpty String uniqueName, @NotEmpty String name, @NotEmpty String containerFrom, @NotEmpty String containerTo) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.containerFrom = containerFrom;
        this.containerTo = containerTo;
    }

    public Link(@NotEmpty String uniqueName, @NotEmpty String name, @NotEmpty String containerFrom, @NotEmpty String containerTo, String scheme) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.containerFrom = containerFrom;
        this.containerTo = containerTo;
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainerFrom() {
        return containerFrom;
    }

    public void setContainerFrom(String containerFrom) {
        this.containerFrom = containerFrom;
    }

    public String getContainerTo() {
        return containerTo;
    }

    public void setContainerTo(String containerTo) {
        this.containerTo = containerTo;
    }
}
