package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityContext {
    private Boolean allowPrivilegeEscalation;
    private Capabilities capabilities;
    private Integer runAsUser;
    private Boolean privileged;

    public SecurityContext() {
    }

    public SecurityContext(Boolean privileged) {
        this.privileged = privileged;
    }

    public Boolean getAllowPrivilegeEscalation() {
        return allowPrivilegeEscalation;
    }

    public void setAllowPrivilegeEscalation(Boolean allowPrivilegeEscalation) {
        this.allowPrivilegeEscalation = allowPrivilegeEscalation;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Integer getRunAsUser() {
        return runAsUser;
    }

    public void setRunAsUser(Integer runAsUser) {
        this.runAsUser = runAsUser;
    }

    public Boolean getPrivileged() {
        return privileged;
    }

    public void setPrivileged(Boolean privileged) {
        this.privileged = privileged;
    }
}
