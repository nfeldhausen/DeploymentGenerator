package de.th.bingen.master.backend.model.kubernetes.container.probe;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.container.TCPSocketAction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Probe {
    private int failureThreshold = 3;
    private HTTPGetAction httpGet;
    private TCPSocketAction tcpSocket;
    private ExecAction exec;
    private int initialDelaySeconds = 10;
    private int periodSeconds = 10;
    private int successThreshold = 1;
    private int timeoutSeconds = 1;

    public Probe() {
    }

    public Probe(int failureThreshold, int initialDelaySeconds, int periodSeconds, int successThreshold, int timeoutSeconds, String path, int port) {
        this.failureThreshold = failureThreshold;
        this.httpGet = new HTTPGetAction(path, port);
        this.initialDelaySeconds = initialDelaySeconds;
        this.periodSeconds = periodSeconds;
        this.successThreshold = successThreshold;
        this.timeoutSeconds = timeoutSeconds;
    }

    public Probe(int failureThreshold, int initialDelaySeconds, int periodSeconds, int successThreshold, int timeoutSeconds, int port) {
        this.failureThreshold = failureThreshold;
        this.tcpSocket = new TCPSocketAction(port);
        this.initialDelaySeconds = initialDelaySeconds;
        this.periodSeconds = periodSeconds;
        this.successThreshold = successThreshold;
        this.timeoutSeconds = timeoutSeconds;
    }

    public ExecAction getExec() {
        return exec;
    }

    public void setExec(ExecAction exec) {
        this.exec = exec;
    }

    public TCPSocketAction getTcpSocket() {
        return tcpSocket;
    }

    public void setTcpSocket(TCPSocketAction tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    public int getFailureThreshold() {
        return failureThreshold;
    }

    public void setFailureThreshold(int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public HTTPGetAction getHttpGet() {
        return httpGet;
    }

    public void setHttpGet(HTTPGetAction httpGet) {
        this.httpGet = httpGet;
    }

    public int getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public void setInitialDelaySeconds(int initialDelaySeconds) {
        this.initialDelaySeconds = initialDelaySeconds;
    }

    public int getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(int periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

    public int getSuccessThreshold() {
        return successThreshold;
    }

    public void setSuccessThreshold(int successThreshold) {
        this.successThreshold = successThreshold;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
