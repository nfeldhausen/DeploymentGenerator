package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpIngressRuleValue {
    private List<HttpIngressPath> paths;

    public HttpIngressRuleValue() {
    }

    public HttpIngressRuleValue(List<HttpIngressPath> paths) {
        this.paths = paths;
    }

    public List<HttpIngressPath> getPaths() {
        return paths;
    }

    public void setPaths(List<HttpIngressPath> paths) {
        this.paths = paths;
    }

    public void addPath(String serviceName, int port, String subpath) {
        if (paths == null) {
            paths = new ArrayList<>();
        }

        paths.add(new HttpIngressPath(serviceName, port, subpath));
    }
}
