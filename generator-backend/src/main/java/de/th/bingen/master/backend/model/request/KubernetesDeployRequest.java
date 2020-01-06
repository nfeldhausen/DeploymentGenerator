package de.th.bingen.master.backend.model.request;

import javax.validation.constraints.NotEmpty;

public class KubernetesDeployRequest {
    @NotEmpty
    private String host;
    @NotEmpty
    private String token;
    @NotEmpty
    private String deployment;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeployment() {
        return deployment;
    }

    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }
}
