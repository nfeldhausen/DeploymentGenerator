package de.th.bingen.master.backend.model.kubernetes.pod;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.affinity.Affinity;
import de.th.bingen.master.backend.model.kubernetes.container.Container;
import de.th.bingen.master.backend.model.kubernetes.volumes.Volume;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodSpec {
    private Integer terminationGracePeriodSeconds;
    private String serviceAccountName;
    private Affinity affinity;
    private List<Container> containers = new ArrayList<>();
    private List<Container> initContainers;
    private String restartPolicy;
    private List<Volume> volumes;

    public PodSpec() {
    }

    public PodSpec(List<Container> containers) {
        this.containers = containers;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public String getRestartPolicy() {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    public List<Container> getInitContainers() {
        return initContainers;
    }

    public void setInitContainers(List<Container> initContainers) {
        this.initContainers = initContainers;
    }

    public Affinity getAffinity() {
        return affinity;
    }

    public void setAffinity(Affinity affinity) {
        this.affinity = affinity;
    }

    public String getServiceAccountName() {
        return serviceAccountName;
    }

    public void setServiceAccountName(String serviceAccountName) {
        this.serviceAccountName = serviceAccountName;
    }

    public Integer getTerminationGracePeriodSeconds() {
        return terminationGracePeriodSeconds;
    }

    public void setTerminationGracePeriodSeconds(Integer terminationGracePeriodSeconds) {
        this.terminationGracePeriodSeconds = terminationGracePeriodSeconds;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public void addVolume(Volume volume) {
        if (volumes == null) {
            volumes = new ArrayList<>();
        }

        volumes.add(volume);
    }
}
