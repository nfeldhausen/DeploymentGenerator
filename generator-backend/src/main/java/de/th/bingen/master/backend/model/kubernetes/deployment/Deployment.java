package de.th.bingen.master.backend.model.kubernetes.deployment;

import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

public class Deployment extends BaseClass {
    private DeploymentSpec spec;

    public Deployment() {
        setApiVersion("apps/v1");
        setKind("Deployment");
    }

    public Deployment(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
    }

    public DeploymentSpec getSpec() {
        return spec;
    }

    public void setSpec(DeploymentSpec spec) {
        this.spec = spec;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        getMetadata().setLabels(labels);
        spec.setAllLabels(labels);
    }
}
