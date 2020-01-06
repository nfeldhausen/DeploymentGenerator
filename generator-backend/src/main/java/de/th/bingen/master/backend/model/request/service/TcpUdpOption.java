package de.th.bingen.master.backend.model.request.service;

import de.th.bingen.master.backend.model.request.Port;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class TcpUdpOption {
    @NotEmpty
    private String containerName;
    @Valid
    private Port port;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }
}
