package de.th.bingen.master.backend.model.request.service;

import de.th.bingen.master.backend.model.request.enums.ServiceType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Service {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
    private String name;
    @NotNull
    private ServiceType type;
    @Valid
    private NormalServiceOptions normalServiceOptions;
    @Valid
    private IngressOptions ingressOptions;

    public NormalServiceOptions getNormalServiceOptions() {
        return normalServiceOptions;
    }

    public void setNormalServiceOptions(NormalServiceOptions normalServiceOptions) {
        this.normalServiceOptions = normalServiceOptions;
    }

    public IngressOptions getIngressOptions() {
        return ingressOptions;
    }

    public void setIngressOptions(IngressOptions ingressOptions) {
        this.ingressOptions = ingressOptions;
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

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public void setTypeAdvanced(ServiceType type) {
        this.type = type;

        if (type == ServiceType.NodePort || type == ServiceType.LoadBalancer) {
            normalServiceOptions = new NormalServiceOptions();
            ingressOptions = null;
        } else {
            normalServiceOptions = null;
            ingressOptions = new IngressOptions();
        }
    }
}
