package de.th.bingen.master.backend.services;

import de.th.bingen.master.backend.model.kubernetes.LabelSelector;
import de.th.bingen.master.backend.model.kubernetes.container.Container;
import de.th.bingen.master.backend.model.kubernetes.container.ContainerPort;
import de.th.bingen.master.backend.model.kubernetes.deployment.Deployment;
import de.th.bingen.master.backend.model.kubernetes.deployment.DeploymentSpec;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvVar;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.*;
import de.th.bingen.master.backend.model.kubernetes.pod.*;
import de.th.bingen.master.backend.model.kubernetes.service.ServicePort;
import de.th.bingen.master.backend.model.kubernetes.service.ServiceSpec;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSet;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSetSpec;
import de.th.bingen.master.backend.model.request.enums.ServiceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class KubernetesYAMLService {

    /**
     * Creates a statefulset
     *
     * @param name The name of the Statefulset
     * @param podLabels The labels of the Kubernetes pods
     * @param replicas The count of instances the stateful set should get
     * @param selector The selector for the pods
     * @param serviceName The name of the service for the stateful set
     * @param containerName The name of the container
     * @param containerImage The image of the statefulsets containers
     * @param envVars The environment variables of the statefulsets containers
     * @param containerPorts The ports of the statefulsets containers
     * @return A deployable statefulset
     */
    public StatefulSet getStatefulSet(String name, HashMap<String, String> podLabels, int replicas, LabelSelector selector, String serviceName, String containerName,
                                      String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        StatefulSet resultSet = new StatefulSet(name, podLabels);

        StatefulSetSpec statefulSetSpec = getStatefulSetSpec(replicas, selector, serviceName, podLabels, containerName, containerImage, envVars, containerPorts);
        resultSet.setSpec(statefulSetSpec);

        return resultSet;
    }

    /**
     * Creates a spec for a StatefulSet
     *
     * @param replicas The count of instances the stateful set should get
     * @param selector The selector for the pods
     * @param serviceName The name of the service for the stateful set
     * @param podLabels The labels of the Kubernetes pods
     * @param containerName The name of the container
     * @param containerImage The image of the statefulsets containers
     * @param envVars The environment variables of the statefulsets containers
     * @param containerPorts The ports of the statefulsets containers
     * @return The spec for a StatefulSet
     */
    public StatefulSetSpec getStatefulSetSpec(int replicas, LabelSelector selector, String serviceName, HashMap<String, String> podLabels,
                                              String containerName, String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        StatefulSetSpec resultSpec = new StatefulSetSpec();

        resultSpec.setReplicas(replicas);
        resultSpec.setSelector(selector);
        resultSpec.setServiceName(serviceName);

        PodTemplateSpec podTemplateSpec = getPodTemplateSpec(podLabels, containerName, containerImage, envVars, containerPorts);
        resultSpec.setTemplate(podTemplateSpec);

        return resultSpec;
    }

    /**
     * Creates a Deployment with a Deployment Spec
     *
     * @param name The name of the deployment
     * @param podLabels The labels of the deployments containers
     * @param replicas The count of instances for the deployment
     * @param selector The selector for the pods
     * @param containerName The name for the deployments containers
     * @param containerImage The image for the deployments containers
     * @param envVars The environment variables for the deployments containers
     * @param containerPorts The ports for the deployments containers
     * @return A Deployment with a Deployment Spec
     */
    public Deployment getDeployment(String name, HashMap<String, String> podLabels, int replicas, LabelSelector selector, String containerName, String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        Deployment resultDeployment = new Deployment(name, podLabels);

        DeploymentSpec deploymentSpec = getDeploymentSpec(replicas,selector,podLabels,containerName,containerImage,envVars,containerPorts);
        resultDeployment.setSpec(deploymentSpec);

        return resultDeployment;
    }

    /**
     * Creates a Deployment Spec
     *
     * @param replicas The count of instances for the deployment
     * @param selector The selector for the pods
     * @param podLabels The labels of the deployments containers
     * @param containerName The name for the deployments containers
     * @param containerImage The image for the deployments containers
     * @param envVars The environment variables for the deployments containers
     * @param containerPorts The ports for the deployments containers
     * @return A Deployment Spec
     */
    public DeploymentSpec getDeploymentSpec(int replicas, LabelSelector selector, HashMap<String, String> podLabels, String containerName, String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        DeploymentSpec deploymentSpec = new DeploymentSpec();

        deploymentSpec.setSelector(selector);
        deploymentSpec.setReplicas(replicas);

        PodTemplateSpec podTemplateSpec = getPodTemplateSpec(podLabels, containerName, containerImage, envVars, containerPorts);

        deploymentSpec.setTemplate(podTemplateSpec);

        return deploymentSpec;
    }


    /**
     * Creates a PodTemplateSpec with a PodSpec
     *
     * @param podLabels The labels for the pods
     * @param containerName The name of the container
     * @param containerImage The image of the container
     * @param envVars The environment variables of the container
     * @param containerPorts The ports of the container
     * @return A PodTemplateSpec
     */
    public PodTemplateSpec getPodTemplateSpec(HashMap<String, String> podLabels, String containerName, String containerImage,
                                              List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        PodTemplateSpec resultSpec = new PodTemplateSpec(podLabels);

        PodSpec podSpec = getPodSpec(containerName, containerImage, envVars, containerPorts);
        resultSpec.setSpec(podSpec);

        return resultSpec;
    }

    /**
     * Creates a PodSpec with a single container
     *
     * @param containerName The name of the container
     * @param containerImage The image of the container
     * @param envVars The environment variables of the container
     * @param containerPorts The ports of the container
     * @return A PodSpec
     */
    public PodSpec getPodSpec(String containerName, String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        PodSpec resultSpec = new PodSpec();

        List<Container> containers = new ArrayList<>();
        Container container = getContainer(containerName, containerImage, envVars, containerPorts);

        containers.add(container);
        resultSpec.setContainers(containers);

        return resultSpec;
    }

    /**
     * Creates a Container
     *
     * @param containerName The name of the container
     * @param containerImage The image of the container
     * @param envVars The environment variables of the container
     * @param containerPorts The ports of the container
     * @return
     */
    public Container getContainer(String containerName, String containerImage, List<EnvVar> envVars, List<ContainerPort> containerPorts) {
        Container resultContainer = new Container();

        resultContainer.setName(containerName);
        resultContainer.setImage(containerImage);

        if (envVars != null && !envVars.isEmpty()) {
            resultContainer.setEnv(envVars);
        }

        if (containerPorts != null && !containerPorts.isEmpty()) {
            resultContainer.setPorts(containerPorts);
        }

        return resultContainer;
    }

    /**
     * Creates a Persistent Volume Claim with the given size and name
     *
     * @param name The name of the persistent volume claim
     * @param volumeSize the size of the persistent volume claim
     * @param labels The labels for the persistent volume claim
     * @return A persistent volume claim
     */
    public PersistentVolumeClaim getPersistentVolumeClaim(String name, int volumeSize, HashMap<String, String> labels) {
        PersistentVolumeClaim resultClaim = new PersistentVolumeClaim(name, labels);

        PersistentVolumeClaimSpec persistentVolumeClaimSpec = getPersistentVolumeClaimSpec(volumeSize);

        resultClaim.setSpec(persistentVolumeClaimSpec);

        return resultClaim;
    }

    /**
     * Creates a Persistent Volume Claim Spec with the given size
     *
     * @param volumeSize The size for the storage
     * @return The persistent volume claim spec
     */
    public PersistentVolumeClaimSpec getPersistentVolumeClaimSpec(int volumeSize) {
        PersistentVolumeClaimSpec resultSpec = new PersistentVolumeClaimSpec();

        resultSpec.setAccessModes(Arrays.asList(DiskAccessMode.ReadWriteOnce));

        ResourceRequirements resourceRequirements = getResourceRequirements(null,null,volumeSize + "",null,null,null);

        resultSpec.setResources(resourceRequirements);

        return resultSpec;
    }

    /**
     * Creates Resource Requirements for persistent volume claims or for container resource limits
     *
     * @param rCpu request for the cpu (in form 100m or 0.1 for example)
     * @param rMemory request for the memory in megabyte
     * @param rStorage request for the storage in megabyte
     * @param lCpu limits for the cpu (in form 100m or 0.1 for example)
     * @param lMemory limits for the memory in megabyte
     * @param lStorage limits for the storage in megabyte
     * @return A Resource Requirement
     */
    public ResourceRequirements getResourceRequirements(String rCpu, String rMemory, String rStorage, String lCpu, String lMemory, String lStorage) {
        ResourceRequirements resultRequirements = new ResourceRequirements();

        resultRequirements.setRequests(getResourceRequirementsObject(rCpu, rMemory, rStorage));
        resultRequirements.setLimits(getResourceRequirementsObject(lCpu, lMemory, lStorage));

        return resultRequirements;
    }

    /**
     * Creates a Resource Requirement object
     *
     * @param cpu requirement for the cpu (in form 100m or 0.1 for example)
     * @param memory requirement for the memory in megabyte
     * @param storage requirement for the storage in megabyte
     * @return A Resource Requirement Object
     */
    public ResourceRequirementsObject getResourceRequirementsObject(String cpu, String memory, String storage) {
        if (cpu == null && memory == null && storage == null) {
            return null;
        }

        ResourceRequirementsObject resultObject = new ResourceRequirementsObject();
        resultObject.setCpu(cpu);
        resultObject.setMemory((memory == null ? null : memory + "Mi"));
        resultObject.setStorage((storage == null ? null : storage + "Mi"));

        return resultObject;
    }

    /**
     * Creates a Service
     *
     * @param name The name of the service
     * @param labels The labels of the service and the labels of pods which to select
     * @param serviceType The ServiceType of the Service
     * @param sessionAffinity Sets Session Affinity or not (Possible Values: ClientIP and None)
     * @param servicePorts List of ports for the service
     * @return The service
     */
    public de.th.bingen.master.backend.model.kubernetes.service.Service getService(String name, HashMap<String, String> labels, ServiceType serviceType, String sessionAffinity, List<ServicePort> servicePorts) {
        de.th.bingen.master.backend.model.kubernetes.service.Service resultService = new de.th.bingen.master.backend.model.kubernetes.service.Service(name, labels);

        ServiceSpec serviceSpec = getServiceSpec(labels, serviceType, sessionAffinity, servicePorts);
        resultService.setSpec(serviceSpec);

        return resultService;
    }

    /**
     * Creates a Service Spec
     *
     * @param labels The labels of the pods to select
     * @param serviceType The ServiceType of the service
     * @param sessionAffinity Sets Session Affinity or not (Possible Values: ClientIP and None)
     * @param servicePorts List of ports for the service
     * @return The service spec
     */
    public ServiceSpec getServiceSpec(HashMap<String, String> labels, ServiceType serviceType, String sessionAffinity, List<ServicePort> servicePorts) {
        ServiceSpec resultSpec = new ServiceSpec();

        resultSpec.setSelector(labels);
        resultSpec.setType(serviceType);
        resultSpec.setPorts(servicePorts);
        resultSpec.setSessionAffinity(sessionAffinity);

        return resultSpec;
    }
}
