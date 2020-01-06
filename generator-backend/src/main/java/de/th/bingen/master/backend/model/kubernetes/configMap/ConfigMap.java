package de.th.bingen.master.backend.model.kubernetes.configMap;

import de.th.bingen.master.backend.model.kubernetes.BaseClass;
import de.th.bingen.master.backend.model.kubernetes.ObjectMetadata;

import java.util.HashMap;

public class ConfigMap extends BaseClass {
    HashMap<String, String> data;

    public ConfigMap() {
        setApiVersion("v1");
        setKind("ConfigMap");
    }

    public ConfigMap(String name, HashMap<String, String> labels) {
        this();
        setMetadata(new ObjectMetadata(name, labels));
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
