package de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceRequirementsObject {
    private String cpu;
    private String memory;
    private String storage;

    public ResourceRequirementsObject() {
    }

    public ResourceRequirementsObject(Double cpu, Integer memory) {
        if (cpu != null) {
            this.cpu = cpu + "";
        }

        if (memory != null) {
            this.memory = memory + "Mi";
        }
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }
}
