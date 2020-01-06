package de.th.bingen.master.backend.model.kubernetes.deployment;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentStrategy {
    private DeploymentStrategyType type;

    public DeploymentStrategy() {
    }

    public DeploymentStrategy(DeploymentStrategyType type) {
        this.type = type;
    }

    public DeploymentStrategyType getType() {
        return type;
    }

    public void setType(DeploymentStrategyType type) {
        this.type = type;
    }
}
