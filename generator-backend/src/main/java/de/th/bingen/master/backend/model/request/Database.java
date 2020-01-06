package de.th.bingen.master.backend.model.request;

import de.th.bingen.master.backend.model.request.container.EnvironmentVariable;
import de.th.bingen.master.backend.model.request.enums.ServiceType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.List;

public class Database {
    @NotEmpty
    private String uniqueName;
    @NotEmpty
    @Pattern(regexp = "([A-Za-z0-9][-A-Za-z0-9_.]*)?[A-Za-z0-9]")
    private String name;
    @NotEmpty
    private String image;
    @Positive
    private int size;
    private boolean expose;
    private ServiceType type = ServiceType.LoadBalancer;
    private List<EnvironmentVariable> environments;

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

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isExpose() {
        return expose;
    }

    public void setExpose(boolean expose) {
        this.expose = expose;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
