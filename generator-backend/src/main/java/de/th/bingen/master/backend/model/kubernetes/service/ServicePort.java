package de.th.bingen.master.backend.model.kubernetes.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicePort {
    private String name;
    private Integer port;
    private Integer targetPort;
    private ProtocolType protocol = ProtocolType.TCP;

    public ServicePort() {
    }

    public ServicePort(String name, int port, int targetPort, ProtocolType protocol) {
        this.name = name;
        this.port = port;
        this.targetPort = targetPort;
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public ProtocolType getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }
}
