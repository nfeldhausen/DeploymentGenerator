package de.th.bingen.master.backend.model.request;

import de.th.bingen.master.backend.helper.RandomWord;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Port {
    @NotEmpty
    private String uniqueName;
    @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
    private String name;
    @Range(min = 1,max = 65535)
    private int port;
    @NotNull
    private ProtocolType protocol;
    @Range(min = 1,max = 65535)
    private Integer targetPort;

    public Port() {
        this.uniqueName = "p-" + RandomWord.getRandomWordCombination();
    }

    public Port(@NotEmpty String uniqueName, @Pattern(regexp = "[0-9]*[a-z]+[a-z0-9]*") String name, @Range(min = 1, max = 65535) int port, @NotNull ProtocolType protocol) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.port = port;
        this.protocol = protocol;
    }

    public Port(@NotEmpty String uniqueName, @Pattern(regexp = "[0-9]*[a-z]+[a-z0-9]*") String name, @Range(min = 1, max = 65535) int port, @Range(min = 1, max = 65535) Integer targetPort, @NotNull ProtocolType protocol) {
        this.uniqueName = uniqueName;
        this.name = name;
        this.port = port;
        this.protocol = protocol;
        this.targetPort = targetPort;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ProtocolType getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolType protocol) {
        this.protocol = protocol;
    }
}
