package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TCPSocketAction {
    private int port;

    public TCPSocketAction() {
    }

    public TCPSocketAction(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
