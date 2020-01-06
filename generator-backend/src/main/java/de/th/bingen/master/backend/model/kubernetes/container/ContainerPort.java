package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerPort {
    private int containerPort;
    private String name;
    private ProtocolType protocol = ProtocolType.TCP;

    public ContainerPort() {
    }

    public ContainerPort(int containerPort, String name, ProtocolType protocol) {
        this.containerPort = containerPort;
        this.name = name;
        this.protocol = protocol;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(int containerPort) {
        this.containerPort = containerPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProtocolType getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }
}
