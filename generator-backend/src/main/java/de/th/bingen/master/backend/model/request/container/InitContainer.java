package de.th.bingen.master.backend.model.request.container;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class InitContainer {
    private String name;
    @NotEmpty
    private String command;
    private String mountPath;
    private boolean privileged;
    @NotEmpty
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
