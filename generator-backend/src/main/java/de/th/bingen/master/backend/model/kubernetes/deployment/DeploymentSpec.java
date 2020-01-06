package de.th.bingen.master.backend.model.kubernetes.deployment;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.LabelSelector;
import de.th.bingen.master.backend.model.kubernetes.pod.PodTemplateSpec;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentSpec {
    private Integer replicas;
    private LabelSelector selector;
    private DeploymentStrategy strategy;
    private PodTemplateSpec template;

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public LabelSelector getSelector() {
        return selector;
    }

    public void setSelector(LabelSelector selector) {
        this.selector = selector;
    }

    public DeploymentStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(DeploymentStrategy strategy) {
        this.strategy = strategy;
    }

    public PodTemplateSpec getTemplate() {
        return template;
    }

    public void setTemplate(PodTemplateSpec template) {
        this.template = template;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        if (selector != null) {
            selector.setMatchLabels(labels);
        }

        template.setAllLabels(labels);
    }
}
