package de.th.bingen.master.backend.model.kubernetes.container.probe;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HTTPGetAction {
    private String path;
    private int port;
    private List<HTTPHeader> httpHeaders;

    public HTTPGetAction() {
    }

    public HTTPGetAction(String path, int port) {
        this.path = path;
        this.port = port;
    }

    public List<HTTPHeader> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(List<HTTPHeader> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
