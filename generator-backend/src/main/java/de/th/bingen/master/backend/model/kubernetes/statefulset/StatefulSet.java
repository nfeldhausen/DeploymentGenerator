package de.th.bingen.master.backend.model.kubernetes.statefulset;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.PersistentVolumeClaim;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatefulSet extends BaseClass {
    private StatefulSetSpec spec;

    public StatefulSetSpec getSpec() {
        return spec;
    }

    public void setSpec(StatefulSetSpec spec) {
        this.spec = spec;
    }

    public StatefulSet() {
        setApiVersion("apps/v1");
        setKind("StatefulSet");
    }

    public StatefulSet(String name, HashMap<String, String> labels) {
        this();
        this.setMetadata(new ObjectMetadata(name, labels));
    }

    public void addPersistentVolumeClaimTemplate(PersistentVolumeClaim persistentVolumeClaim) {
        spec.addPersistentVolumeClaimTemplate(persistentVolumeClaim);
    }

    public void addVolumeMount(String pvcName, String mountPath, String subpath) {
        spec.addVolumeMount(pvcName, mountPath, subpath);
    }

    public void setAllLabels(HashMap<String, String> labels) {
        getMetadata().setLabels(labels);
        spec.setAllLabels(labels);
    }
}
