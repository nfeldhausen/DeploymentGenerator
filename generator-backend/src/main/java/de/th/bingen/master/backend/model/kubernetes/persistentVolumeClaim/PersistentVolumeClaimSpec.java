package de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.LabelSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistentVolumeClaimSpec {
    private List<DiskAccessMode> accessModes = new ArrayList<>();
    private ResourceRequirements resources;
    private LabelSelector selector;

    public List<DiskAccessMode> getAccessModes() {
        return accessModes;
    }

    public void setAccessModes(List<DiskAccessMode> accessModes) {
        this.accessModes = accessModes;
    }

    public ResourceRequirements getResources() {
        return resources;
    }

    public void setResources(ResourceRequirements resources) {
        this.resources = resources;
    }

    public LabelSelector getSelector() {
        return selector;
    }

    public void setSelector(LabelSelector selector) {
        this.selector = selector;
    }
}
