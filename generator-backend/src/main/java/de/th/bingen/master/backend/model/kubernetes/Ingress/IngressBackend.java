package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngressBackend {
    private String serviceName;
    private int servicePort;

    public IngressBackend(String serviceName, int servicePort) {
        this.serviceName = serviceName;
        this.servicePort = servicePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }
}
