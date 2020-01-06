package de.th.bingen.master.backend.model.kubernetes;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectMetadata {
    private String name;
    private String namespace;
    private HashMap<String, String> labels;
    private HashMap<String, String> annotations;

    public ObjectMetadata() {
    }

    public ObjectMetadata(HashMap<String, String> labels) {
        this.labels = labels;
    }

    public ObjectMetadata(String name, HashMap<String, String> labels) {
        this.name = name;
        this.labels = labels;
    }

    public HashMap<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(HashMap<String, String> annotations) {
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public HashMap<String, String> getLabels() {
        return labels;
    }

    public void setLabels(HashMap<String, String> labels) {
        this.labels = labels;
    }

    public void addAnnotation(String key, String annotation) {
        if (annotations == null) {
            annotations = new HashMap<>();
        }

        annotations.put(key, annotation);
    }
}
