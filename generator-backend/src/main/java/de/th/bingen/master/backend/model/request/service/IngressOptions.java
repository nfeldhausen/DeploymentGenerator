package de.th.bingen.master.backend.model.request.service;

import de.th.bingen.master.backend.model.request.enums.ServiceType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


public class IngressOptions {
    @NotEmpty
    private String uniqueName;
    private boolean affinity;
    @NotNull
    private ServiceType serviceType;
    @Valid
    private List<HttpOption> httpOptions;
    @Valid
    private List<TcpUdpOption> tcpUdpOptions;

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public boolean isAffinity() {
        return affinity;
    }

    public void setAffinity(boolean affinity) {
        this.affinity = affinity;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<HttpOption> getHttpOptions() {
        return httpOptions;
    }

    public void setHttpOptions(List<HttpOption> httpOptions) {
        this.httpOptions = httpOptions;
    }

    public List<TcpUdpOption> getTcpUdpOptions() {
        return tcpUdpOptions;
    }

    public void setTcpUdpOptions(List<TcpUdpOption> tcpUdpOptions) {
        this.tcpUdpOptions = tcpUdpOptions;
    }
}
