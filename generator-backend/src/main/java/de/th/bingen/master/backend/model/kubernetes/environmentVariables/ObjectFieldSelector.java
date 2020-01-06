package de.th.bingen.master.backend.model.kubernetes.environmentVariables;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectFieldSelector {
    private String fieldPath;

    public ObjectFieldSelector() {
    }

    public ObjectFieldSelector(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }
}
