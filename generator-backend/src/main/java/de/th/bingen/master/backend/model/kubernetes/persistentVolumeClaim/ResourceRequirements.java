package de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceRequirements {
    private ResourceRequirementsObject limits;
    private ResourceRequirementsObject requests;

    public ResourceRequirementsObject getLimits() {
        return limits;
    }

    public void setLimits(ResourceRequirementsObject limits) {
        this.limits = limits;
    }

    public ResourceRequirementsObject getRequests() {
        return requests;
    }

    public void setRequests(ResourceRequirementsObject requests) {
        this.requests = requests;
    }

    public void setLimits(Double cpu, Integer memory) {
        limits = new ResourceRequirementsObject(cpu, memory);
    }

    public void setRequests(Double cpu, Integer memory) {
        requests = new ResourceRequirementsObject(cpu,memory);
    }
}
