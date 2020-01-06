package de.th.bingen.master.backend.model.kubernetes.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service extends BaseClass {
    private ServiceSpec spec;

    public Service() {
        setApiVersion("v1");
        setKind("Service");
    }

    public Service(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
    }

    public ServiceSpec getSpec() {
        return spec;
    }

    public void setSpec(ServiceSpec spec) {
        this.spec = spec;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        getMetadata().setLabels(labels);
        spec.setAllLabels(labels);
    }
}
