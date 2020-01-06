package de.th.bingen.master.backend.model.kubernetes.container.lifecycle;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.container.probe.ExecAction;
import de.th.bingen.master.backend.model.kubernetes.container.probe.HTTPGetAction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Handler {
    private ExecAction exec;
    private HTTPGetAction httpGet;

    public Handler() {}

    public Handler(String singleCommand) {
        exec = new ExecAction();

        exec.addCommand(singleCommand);
    }

    public ExecAction getExec() {
        return exec;
    }

    public void setExec(ExecAction exec) {
        this.exec = exec;
    }

    public HTTPGetAction getHttpGet() {
        return httpGet;
    }

    public void setHttpGet(HTTPGetAction httpGet) {
        this.httpGet = httpGet;
    }
}
