package de.th.bingen.master.backend.model.kubernetes.volumes;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Volume {
    private String name;
    private EmptyDirVolumeSource emptyDir;

    public Volume() {
    }

    public Volume(String name, EmptyDirVolumeSource emptyDir) {
        this.name = name;
        this.emptyDir = emptyDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmptyDirVolumeSource getEmptyDir() {
        return emptyDir;
    }

    public void setEmptyDir(EmptyDirVolumeSource emptyDir) {
        this.emptyDir = emptyDir;
    }
}
