package de.th.bingen.master.backend.model.kubernetes.statefulset;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.LabelSelector;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.PersistentVolumeClaim;
import de.th.bingen.master.backend.model.kubernetes.container.Container;
import de.th.bingen.master.backend.model.kubernetes.pod.PodTemplateSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatefulSetSpec {
    private Integer replicas;
    private LabelSelector selector;
    private String serviceName;
    private PodTemplateSpec template;
    private List<PersistentVolumeClaim> volumeClaimTemplates;

    public List<PersistentVolumeClaim> getVolumeClaimTemplates() {
        return volumeClaimTemplates;
    }

    public void setVolumeClaimTemplates(List<PersistentVolumeClaim> volumeClaimTemplates) {
        this.volumeClaimTemplates = volumeClaimTemplates;
    }

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public PodTemplateSpec getTemplate() {
        return template;
    }

    public void setTemplate(PodTemplateSpec template) {
        this.template = template;
    }

    public void addPersistentVolumeClaimTemplate(PersistentVolumeClaim persistentVolumeClaim) {
        if (volumeClaimTemplates == null) {
            volumeClaimTemplates = new ArrayList<>();
        }

        volumeClaimTemplates.add(persistentVolumeClaim);
    }

    public void addVolumeMount(String pvcName, String mountPath, String subpath) {
        Container container = template.getSpec().getContainers().get(0);

        container.addVolumeMount(pvcName, mountPath, subpath);
    }

    public void setAllLabels(HashMap<String, String> labels) {
        if (selector != null) {
            selector.setMatchLabels(labels);
        }
        template.setAllLabels(labels);

        for (PersistentVolumeClaim pvc: volumeClaimTemplates) {
            pvc.setAllLabels(labels);
        }
    }
}
