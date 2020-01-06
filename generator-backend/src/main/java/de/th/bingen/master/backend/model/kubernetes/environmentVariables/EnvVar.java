package de.th.bingen.master.backend.model.kubernetes.environmentVariables;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnvVar {
    private String name;
    private String value;
    private EnvVarSource valueFrom;

    public EnvVar() {
    }

    public EnvVar(String name) {
        this.name = name;
    }

    public EnvVar(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EnvVarSource getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(EnvVarSource valueFrom) {
        this.valueFrom = valueFrom;
    }

    public void setValueFromFieldRef(String value) {
        EnvVarSource envVarSource = new EnvVarSource();
        envVarSource.addFieldRef(value);

        valueFrom = envVarSource;
    }
}
