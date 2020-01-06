package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpIngressPath {
    private IngressBackend backend;
    private String path;

    public HttpIngressPath() {
    }

    public HttpIngressPath(String serviceName, int port, String subPath) {
        this.backend = new IngressBackend(serviceName, port);
        this.path = subPath;
    }

    public IngressBackend getBackend() {
        return backend;
    }

    public void setBackend(IngressBackend backend) {
        this.backend = backend;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
