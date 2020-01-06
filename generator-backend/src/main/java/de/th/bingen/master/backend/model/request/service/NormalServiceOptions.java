package de.th.bingen.master.backend.model.request.service;

import de.th.bingen.master.backend.helper.RandomWord;
import de.th.bingen.master.backend.model.request.Port;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;
import de.th.bingen.master.backend.model.request.enums.ServiceType;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class NormalServiceOptions {
    @NotEmpty
    private String uniqueName = RandomWord.getRandomWordCombination();
    @NotEmpty
    private String containerName;
    @Valid
    @NotEmpty
    private List<Port> ports = new ArrayList<>();
    private boolean sessionAffinity;

    public boolean isSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(boolean sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public void addPort(String name, int port, Integer targetPort, ProtocolType type) {
        ports.add(new Port(RandomWord.getRandomWordCombination(),name,port,targetPort,type));
    }
}
