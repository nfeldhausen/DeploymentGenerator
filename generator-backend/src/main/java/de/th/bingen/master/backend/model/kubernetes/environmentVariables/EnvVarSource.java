package de.th.bingen.master.backend.model.kubernetes.environmentVariables;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnvVarSource {
    private ObjectFieldSelector fieldRef;
    private ResourceFieldSelector resourceFieldRef;

    public ObjectFieldSelector getFieldRef() {
        return fieldRef;
    }

    public ResourceFieldSelector getResourceFieldRef() {
        return resourceFieldRef;
    }

    public void setResourceFieldRef(ResourceFieldSelector resourceFieldRef) {
        this.resourceFieldRef = resourceFieldRef;
    }

    public void setFieldRef(ObjectFieldSelector fieldRef) {
        this.fieldRef = fieldRef;
    }

    public void addFieldRef(String fieldRef) {
        this.fieldRef = new ObjectFieldSelector(fieldRef);
    }
}
