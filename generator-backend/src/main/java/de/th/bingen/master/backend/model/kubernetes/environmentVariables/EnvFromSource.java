package de.th.bingen.master.backend.model.kubernetes.environmentVariables;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnvFromSource {
    private ConfigMapEnvSource configMapRef;
    private SecretEnvSource secretRef;

    public EnvFromSource() {
    }

    public EnvFromSource(ConfigMapEnvSource configMapRef) {
        this.configMapRef = configMapRef;
    }

    public EnvFromSource(SecretEnvSource secretRef) {
        this.secretRef = secretRef;
    }

    public ConfigMapEnvSource getConfigMapRef() {
        return configMapRef;
    }

    public void setConfigMapRef(ConfigMapEnvSource configMapRef) {
        this.configMapRef = configMapRef;
    }

    public SecretEnvSource getSecretRef() {
        return secretRef;
    }

    public void setSecretRef(SecretEnvSource secretRef) {
        this.secretRef = secretRef;
    }

    public void addConfigMapRef(String name) {
        this.configMapRef = new ConfigMapEnvSource(name);
    }

    public void addSecretRef(String name) {
        this.secretRef = new SecretEnvSource(name);
    }
}
