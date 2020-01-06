package de.th.bingen.master.backend.model.request.container;

import javax.validation.constraints.Positive;

public class RequestResource {
    @Positive
    private Double cpu;
    @Positive
    private Integer memory;

    public RequestResource() {
    }

    public RequestResource(@Positive Double cpu, @Positive Integer memory) {
        this.cpu = cpu;
        this.memory = memory;
    }

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }
}
