package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolumeMount {
    private String mountPath;
    private String name;
    private String subPath;

    public VolumeMount() {
    }

    public VolumeMount(String mountPath, String name, String subPath) {
        this.mountPath = mountPath;
        this.name = name;
        this.subPath = subPath;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }
}
