package de.th.bingen.master.backend.model.kubernetes.container.probe;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExecAction {
    List<String> command;

    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    public void addCommand(String s) {
        if (command == null) {
            this.command = new ArrayList<>();
        }

        command.add(s);
    }
}
