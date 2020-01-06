package de.th.bingen.master.backend.model.kubernetes.secret;

import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

public class Secret extends BaseClass {
    private String type;
    private HashMap<String, String> data;

    public Secret() {
        setApiVersion("v1");
        setKind("Secret");
        setType("Opaque");
    }

    public Secret(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public void addData(String key, String value) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put(key,value);
    }
}
