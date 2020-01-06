package de.th.bingen.master.backend.model.kubernetes.pod;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodTemplateSpec {
    private ObjectMetadata metadata;
    private PodSpec spec;

    public PodTemplateSpec() {
    }

    public PodTemplateSpec(HashMap<String, String> labels) {
        this.metadata = new ObjectMetadata(labels);
    }

    public ObjectMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMetadata metadata) {
        this.metadata = metadata;
    }

    public PodSpec getSpec() {
        return spec;
    }

    public void setSpec(PodSpec spec) {
        this.spec = spec;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        metadata.setLabels(labels);
    }
}
