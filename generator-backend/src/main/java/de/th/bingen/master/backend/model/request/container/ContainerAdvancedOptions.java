package de.th.bingen.master.backend.model.request.container;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

public class ContainerAdvancedOptions {
    private boolean httpService = false;
    private Boolean privileged;
    @Valid
    private RequestProbe readinessProbe;
    @Valid
    private RequestProbe livenessProbe;
    @Valid
    private RequestResource requests = new RequestResource();
    @Valid
    private RequestResource limits = new RequestResource();
    @Valid
    private List<InitContainer> initContainers = new ArrayList<>();
    @PositiveOrZero
    private Integer gracePeriod;

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Boolean getPrivileged() {
        return privileged;
    }

    public void setPrivileged(Boolean privileged) {
        this.privileged = privileged;
    }

    public List<InitContainer> getInitContainers() {
        return initContainers;
    }

    public void setInitContainers(List<InitContainer> initContainers) {
        this.initContainers = initContainers;
    }

    public RequestResource getRequests() {
        return requests;
    }

    public void setRequests(RequestResource requests) {
        this.requests = requests;
    }

    public RequestResource getLimits() {
        return limits;
    }

    public void setLimits(RequestResource limits) {
        this.limits = limits;
    }

    public boolean isHttpService() {
        return httpService;
    }

    public void setHttpService(boolean httpService) {
        this.httpService = httpService;
    }

    public RequestProbe getReadinessProbe() {
        return readinessProbe;
    }

    public void setReadinessProbe(RequestProbe readinessProbe) {
        this.readinessProbe = readinessProbe;
    }

    public RequestProbe getLivenessProbe() {
        return livenessProbe;
    }

    public void setLivenessProbe(RequestProbe livenessProbe) {
        this.livenessProbe = livenessProbe;
    }
}
