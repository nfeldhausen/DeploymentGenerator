package de.th.bingen.master.backend.model.kubernetes;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.affinity.LabelSelectorRequirement;

import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelSelector {
    private HashMap<String, String> matchLabels;
    private List<LabelSelectorRequirement> matchExpressions;

    public LabelSelector(HashMap<String, String> matchLabels) {
        this.matchLabels = matchLabels;
    }

    public LabelSelector() {
    }

    public List<LabelSelectorRequirement> getMatchExpressions() {
        return matchExpressions;
    }

    public void setMatchExpressions(List<LabelSelectorRequirement> matchExpressions) {
        this.matchExpressions = matchExpressions;
    }

    public HashMap<String, String> getMatchLabels() {
        return matchLabels;
    }

    public void setMatchLabels(HashMap<String, String> matchLabels) {
        this.matchLabels = matchLabels;
    }
}
