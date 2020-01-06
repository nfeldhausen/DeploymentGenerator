package de.th.bingen.master.backend.model.kubernetes.affinity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeightedPodAffinityTerm {
    private Integer weight;
    private PodAffinityTerm podAffinityTerm;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public PodAffinityTerm getPodAffinityTerm() {
        return podAffinityTerm;
    }

    public void setPodAffinityTerm(PodAffinityTerm podAffinityTerm) {
        this.podAffinityTerm = podAffinityTerm;
    }
}
