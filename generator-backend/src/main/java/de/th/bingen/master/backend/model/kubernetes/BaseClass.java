package de.th.bingen.master.backend.model.kubernetes;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseClass {
    private String apiVersion;
    private String kind;
    private ObjectMetadata metadata;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public ObjectMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMetadata metadata) {
        this.metadata = metadata;
    }

    public void addLabel(String key, String value) {
        if (metadata == null) {
            metadata = new ObjectMetadata();
        }

        if (metadata.getLabels() == null) {
            metadata.setLabels(new HashMap<>());
        }

        metadata.getLabels().put(key, value);
    }
}
