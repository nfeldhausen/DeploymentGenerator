package de.th.bingen.master.backend.model.response;

import de.th.bingen.master.backend.model.kubernetes.Ingress.Ingress;
import de.th.bingen.master.backend.model.kubernetes.configMap.ConfigMap;
import de.th.bingen.master.backend.model.kubernetes.deployment.Deployment;
import de.th.bingen.master.backend.model.kubernetes.deployment.DeploymentSpec;
import de.th.bingen.master.backend.model.kubernetes.secret.Secret;
import de.th.bingen.master.backend.model.kubernetes.service.Service;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSet;

import java.util.ArrayList;
import java.util.List;


public class KubernetesCombined {
    private String templateName;
    private String uniqueName;
    private Deployment deployment;
    private StatefulSet statefulSet;
    private Ingress ingress;
    private List<Service> serviceList = new ArrayList<>();
    private List<ConfigMap> configMapList = new ArrayList<>();
    private List<Secret> secretList = new ArrayList<>();

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<Secret> getSecretList() {
        return secretList;
    }

    public void setSecretList(List<Secret> secretList) {
        this.secretList = secretList;
    }

    public Ingress getIngress() {
        return ingress;
    }

    public void setIngress(Ingress ingress) {
        this.ingress = ingress;
    }

    public StatefulSet getStatefulSet() {
        return statefulSet;
    }

    public void setStatefulSet(StatefulSet statefulSet) {
        this.statefulSet = statefulSet;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public void setDeployment(Deployment deployment) {
        this.deployment = deployment;
    }

    public List<ConfigMap> getConfigMapList() {
        return configMapList;
    }

    public void setConfigMapList(List<ConfigMap> configMapList) {
        this.configMapList = configMapList;
    }

    public void addConfigMap(ConfigMap map) {
        configMapList.add(map);
    }

    public void addService(Service service) {
        serviceList.add(service);
    }

    public void addSecret(Secret secret) {secretList.add(secret); }
}
