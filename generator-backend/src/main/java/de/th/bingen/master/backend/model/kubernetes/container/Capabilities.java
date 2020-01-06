package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Capabilities {
    private List<String> add;
    private List<String> drop;

    public List<String> getAdd() {
        return add;
    }

    public void setAdd(List<String> add) {
        this.add = add;
    }

    public List<String> getDrop() {
        return drop;
    }

    public void setDrop(List<String> drop) {
        this.drop = drop;
    }

    public void addAdd(String s) {
        if (add == null) {
            add = new ArrayList<>();
        }

        add.add(s);
    }

    public void addDrop(String s) {
        if (drop == null) {
            drop = new ArrayList<>();
        }

        drop.add(s);
    }
}
