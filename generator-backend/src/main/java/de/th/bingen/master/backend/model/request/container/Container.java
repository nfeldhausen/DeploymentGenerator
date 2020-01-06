package de.th.bingen.master.backend.model.request.container;

import de.th.bingen.master.backend.model.request.Port;
import de.th.bingen.master.backend.model.request.enums.ServiceType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

public class Container {
    private String templateName;
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
    private String name;
    @NotEmpty
    private String image;
    @Positive
    private int replicas;
    @Valid
    private List<EnvironmentVariable> environments = new ArrayList<>();
    @Valid
    private List<Port> ports = new ArrayList<>();
    private boolean expose;
    private ServiceType exposeType = ServiceType.NodePort;
    @Valid
    private List<Storage> storage = new ArrayList<>();
    @Valid
    private ContainerAdvancedOptions containerAdvancedOptions = new ContainerAdvancedOptions();

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public ContainerAdvancedOptions getContainerAdvancedOptions() {
        return containerAdvancedOptions;
    }

    public void setContainerAdvancedOptions(ContainerAdvancedOptions containerAdvancedOptions) {
        this.containerAdvancedOptions = containerAdvancedOptions;
    }

    public List<Storage> getStorage() {
        return storage;
    }

    public void setStorage(List<Storage> storage) {
        this.storage = storage;
    }

    public boolean isExpose() {
        return expose;
    }

    public void setExpose(boolean expose) {
        this.expose = expose;
    }

    public ServiceType getExposeType() {
        return exposeType;
    }

    public void setExposeType(ServiceType exposeType) {
        this.exposeType = exposeType;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
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
}
