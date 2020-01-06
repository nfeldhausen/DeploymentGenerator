package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ingress extends BaseClass {
    private IngressSpec spec;

    public Ingress() {
        setApiVersion("extensions/v1beta1");
        setKind("Ingress");
    }

    public Ingress(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
    }

    public IngressSpec getSpec() {
        return spec;
    }

    public void setSpec(IngressSpec spec) {
        this.spec = spec;
    }
}
