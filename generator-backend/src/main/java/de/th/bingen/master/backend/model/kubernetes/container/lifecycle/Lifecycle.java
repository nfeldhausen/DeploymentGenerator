package de.th.bingen.master.backend.model.kubernetes.container.lifecycle;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lifecycle {
    private Handler postStart;
    private Handler preStop;

    public Lifecycle() {
    }

    public Lifecycle(Handler postStart, Handler preStop) {
        this.postStart = postStart;
        this.preStop = preStop;
    }

    public Handler getPostStart() {
        return postStart;
    }

    public void setPostStart(Handler postStart) {
        this.postStart = postStart;
    }

    public Handler getPreStop() {
        return preStop;
    }

    public void setPreStop(Handler preStop) {
        this.preStop = preStop;
    }
}
