package de.th.bingen.master.backend.model.kubernetes.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.request.enums.ServiceType;

import java.util.HashMap;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceSpec {
    private List<ServicePort> ports;
    private HashMap<String, String> selector;
    private ServiceType type;
    private String sessionAffinity;
    private String clusterIP;

    public String getClusterIP() {
        return clusterIP;
    }

    public void setClusterIP(String clusterIP) {
        this.clusterIP = clusterIP;
    }

    public List<ServicePort> getPorts() {
        return ports;
    }

    public void setPorts(List<ServicePort> ports) {
        this.ports = ports;
    }

    public HashMap<String, String> getSelector() {
        return selector;
    }

    public void setSelector(HashMap<String, String> selector) {
        this.selector = selector;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    public void setAllLabels(HashMap<String, String> labels) {
        selector = labels;
    }
}
