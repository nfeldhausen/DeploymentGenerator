package de.th.bingen.master.backend.model.request.container;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class StorageMount {
    @Pattern(regexp = "\\/([a-z]|[A-Z]|[0-9]|\\/)+")
    @NotEmpty
    private String mountPath;
    @Pattern(regexp = "([a-z]|[A-Z]|[0-9]){1}([a-z]|[A-Z]|[0-9]|\\/)*")
    private String subPath;

    public StorageMount() {
    }

    public StorageMount(@Pattern(regexp = "\\/([a-z]|[A-Z]|[0-9]|\\/)+") @NotEmpty String mountPath, @Pattern(regexp = "([a-z]|[A-Z]|[0-9]){1}([a-z]|[A-Z]|[0-9]|\\/)*") String subPath) {
        this.mountPath = mountPath;
        this.subPath = subPath;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }
}
