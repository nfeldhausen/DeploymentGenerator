package de.th.bingen.master.backend.model.kubernetes.affinity;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.LabelSelector;

import java.awt.*;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PodAffinityTerm {
    private LabelSelector labelSelector;
    private List<String> namespaces;
    private String topologyKey;

    public LabelSelector getLabelSelector() {
        return labelSelector;
    }

    public void setLabelSelector(LabelSelector labelSelector) {
        this.labelSelector = labelSelector;
    }

    public List<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<String> namespaces) {
        this.namespaces = namespaces;
    }

    public String getTopologyKey() {
        return topologyKey;
    }

    public void setTopologyKey(String topologyKey) {
        this.topologyKey = topologyKey;
    }
}
