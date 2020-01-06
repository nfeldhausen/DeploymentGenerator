package de.th.bingen.master.backend.model.kubernetes.container;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.kubernetes.container.lifecycle.Lifecycle;
import de.th.bingen.master.backend.model.kubernetes.container.probe.Probe;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvFromSource;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvVar;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.ResourceRequirements;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Container {
    private String image;
    private String name;
    private List<String> args;
    private List<EnvVar> env;
    private SecurityContext securityContext;
    private List<ContainerPort> ports;
    private Probe livenessProbe;
    private Probe readinessProbe;
    private Lifecycle lifecycle;
    private List<VolumeMount> volumeMounts;
    private List<EnvFromSource> envFrom;
    private ResourceRequirements resources;
    private List<String> command;
    private String imagePullPolicy;

    public String getImagePullPolicy() {
        return imagePullPolicy;
    }

    public void setImagePullPolicy(String imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public ResourceRequirements getResources() {
        return resources;
    }

    public void setResources(ResourceRequirements resources) {
        this.resources = resources;
    }

    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    public List<EnvFromSource> getEnvFrom() {
        return envFrom;
    }

    public void setEnvFrom(List<EnvFromSource> envFrom) {
        this.envFrom = envFrom;
    }

    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Probe getLivenessProbe() {
        return livenessProbe;
    }

    public void setLivenessProbe(Probe livenessProbe) {
        this.livenessProbe = livenessProbe;
    }

    public Probe getReadinessProbe() {
        return readinessProbe;
    }

    public void setReadinessProbe(Probe readinessProbe) {
        this.readinessProbe = readinessProbe;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    public List<VolumeMount> getVolumeMounts() {
        return volumeMounts;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public void setVolumeMounts(List<VolumeMount> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    public List<EnvVar> getEnv() {
        return env;
    }

    public void setEnv(List<EnvVar> env) {
        this.env = env;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContainerPort> getPorts() {
        return ports;
    }

    public void setPorts(List<ContainerPort> ports) {
        this.ports = ports;
    }

    public void addArg(String arg) {
        if (args == null) {
            args = new ArrayList<>();
        }

        args.add(arg);
    }

    public void addVolumeMount(String pvcName, String mountPath, String subpath) {
        if (volumeMounts == null) {
            volumeMounts = new ArrayList<>();
        }

        volumeMounts.add(new VolumeMount(mountPath,pvcName,subpath));
    }

    public void addVolumeMount(VolumeMount mount) {
        if (volumeMounts == null) {
            volumeMounts = new ArrayList<>();
        }

        volumeMounts.add(mount);
    }

    public void addEnvFrom(EnvFromSource source) {
        if (envFrom == null) {
            envFrom = new ArrayList<>();
        }

        envFrom.add(source);
    }

    public void addEnv(EnvVar variable) {
        if (env == null) {
            env = new ArrayList<>();
        }

        env.add(variable);
    }

    @JsonIgnore
    public boolean isPrivileged() {
        if (securityContext == null || securityContext.getPrivileged() == null || securityContext.getPrivileged() == false) {
            return false;
        }

        return true;
    }
}
