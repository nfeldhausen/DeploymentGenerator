package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngressSpec {
    private List<IngressRule> rules;

    public IngressSpec() {
    }

    public IngressSpec(List<IngressRule> rules) {
        this.rules = rules;
    }

    public List<IngressRule> getRules() {
        return rules;
    }

    public void setRules(List<IngressRule> rules) {
        this.rules = rules;
    }
}
