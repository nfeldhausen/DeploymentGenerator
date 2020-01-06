package de.th.bingen.master.backend.model.response;

import java.util.HashMap;
import java.util.List;

public class KubernetesDeployResponse {
    private String status;
    private String message;
    private HashMap<String, List<String>> serviceEndpoints;

    public KubernetesDeployResponse() {
    }

    public KubernetesDeployResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HashMap<String, List<String>> getServiceEndpoints() {
        return serviceEndpoints;
    }

    public void setServiceEndpoints(HashMap<String, List<String>> serviceEndpoints) {
        this.serviceEndpoints = serviceEndpoints;
    }
}
