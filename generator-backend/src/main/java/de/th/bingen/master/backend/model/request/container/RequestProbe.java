package de.th.bingen.master.backend.model.request.container;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class RequestProbe {
    @Positive
    private Integer failureThreshold;
    @PositiveOrZero
    private Integer initialDelaySeconds;
    @Positive
    private Integer periodSeconds;
    @Positive
    private Integer successThreshold;
    @Positive
    private Integer timeoutSeconds;
    private String httpAction;
    @Positive
    private Integer httpPort;
    @Positive
    private Integer tcpPort;

    public RequestProbe() {
    }

    public RequestProbe(@Positive Integer failureThreshold, @PositiveOrZero Integer initialDelaySeconds, @Positive Integer periodSeconds, @Positive Integer successThreshold, @Positive Integer timeoutSeconds, @Positive Integer httpPort) {
        this.failureThreshold = failureThreshold;
        this.initialDelaySeconds = initialDelaySeconds;
        this.periodSeconds = periodSeconds;
        this.successThreshold = successThreshold;
        this.timeoutSeconds = timeoutSeconds;
        this.httpPort = httpPort;
    }

    public RequestProbe(@Positive Integer failureThreshold, @PositiveOrZero Integer initialDelaySeconds, @Positive Integer periodSeconds, @Positive Integer successThreshold, @Positive Integer timeoutSeconds) {
        this.failureThreshold = failureThreshold;
        this.initialDelaySeconds = initialDelaySeconds;
        this.periodSeconds = periodSeconds;
        this.successThreshold = successThreshold;
        this.timeoutSeconds = timeoutSeconds;
        this.httpPort = 80;
    }

    public Integer getFailureThreshold() {
        return failureThreshold;
    }

    public void setFailureThreshold(Integer failureThreshold) {
        this.failureThreshold = (failureThreshold == null ? 1 : failureThreshold);
    }

    public Integer getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public void setInitialDelaySeconds(Integer initialDelaySeconds) {
        this.initialDelaySeconds = (initialDelaySeconds == null ? 0 : initialDelaySeconds);
    }

    public Integer getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(Integer periodSeconds) {
        this.periodSeconds = (periodSeconds == null ? 10 : periodSeconds);
    }

    public Integer getSuccessThreshold() {
        return successThreshold;
    }

    public void setSuccessThreshold(Integer successThreshold) {
        this.successThreshold = (successThreshold == null ? 1 : successThreshold);
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = (timeoutSeconds == null ? 1 : timeoutSeconds);
    }

    public String getHttpAction() {
        return httpAction;
    }

    public void setHttpAction(String httpAction) {
        this.httpAction = httpAction;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = (httpPort == null ? 80 : httpPort);
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }
}
