package de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistentVolumeClaim extends BaseClass {
    private PersistentVolumeClaimSpec spec;

    public PersistentVolumeClaim() {
        setApiVersion("v1");
        setKind("PersistentVolumeClaim");
    }

    public PersistentVolumeClaim(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
    }

    public PersistentVolumeClaimSpec getSpec() {
        return spec;
    }

    public void setSpec(PersistentVolumeClaimSpec spec) {
        this.spec = spec;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        getMetadata().setLabels(labels);
    }
}
