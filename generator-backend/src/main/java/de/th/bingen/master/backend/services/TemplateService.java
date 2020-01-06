package de.th.bingen.master.backend.services;

import de.th.bingen.master.backend.helper.RandomWord;
import de.th.bingen.master.backend.model.Template;
import de.th.bingen.master.backend.model.kubernetes.configMap.ConfigMap;
import de.th.bingen.master.backend.model.kubernetes.container.ContainerPort;
import de.th.bingen.master.backend.model.kubernetes.container.probe.Probe;
import de.th.bingen.master.backend.model.kubernetes.container.VolumeMount;
import de.th.bingen.master.backend.model.kubernetes.deployment.Deployment;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvVar;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.PersistentVolumeClaim;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.ResourceRequirements;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.ResourceRequirementsObject;
import de.th.bingen.master.backend.model.kubernetes.pod.*;
import de.th.bingen.master.backend.model.kubernetes.container.Container;
import de.th.bingen.master.backend.model.kubernetes.secret.Secret;
import de.th.bingen.master.backend.model.kubernetes.service.ServicePort;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSet;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSetSpec;
import de.th.bingen.master.backend.model.request.*;
import de.th.bingen.master.backend.model.request.container.*;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;
import de.th.bingen.master.backend.model.request.enums.ServiceType;
import de.th.bingen.master.backend.model.response.KubernetesCombined;
import de.th.bingen.master.backend.model.response.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TemplateService {
    @Autowired
    private TemplateLoader templateLoader;
    @Autowired
    private Generator generator;

    public List<Template> getTemplates() {
        return templateLoader.getTemplates();
    }

    /**
     * Searches for a given template and returns a TemplateResponse with a deployable YAML and a request object for modification,
     * if the template exists
     *
     * @param name The name of the template
     * @return A TemplateResponse with a deployable YAML and a request object for modificaiton
     * @throws IOException When the template could not be deep copied or the object could not get serialized
     */
    public TemplateResponse getTemplate(String name) throws IOException {
        Template template = findTemplate(name);

        if (template == null) {
            return null;
        }

        for (KubernetesCombined combined: template.getCombinedList()) {
            combined.setUniqueName(RandomWord.getRandomWordCombination());
        }

        TemplateResponse response = new TemplateResponse();
        response.setResult(templateToString(template));
        response.setDescription(template.getDescription());

        Request request = generateRequest(template);
        response.setRequest(request);

        return response;
    }

    /**
     * Generates a microservice request object from a given template
     *
     * @param template The template of which a request object should be created
     * @return The created microservice request object
     */
    private Request generateRequest(Template template) {
        Request request = new Request();
        request.setTemplateName(template.getName());

        for (KubernetesCombined combined: template.getCombinedList()) {
            //Container
            de.th.bingen.master.backend.model.request.container.Container requestContainer = getContainer(combined);
            request.addContainer(requestContainer);

            PodSpec podSpec = (combined.getDeployment() != null ? combined.getDeployment().getSpec().getTemplate().getSpec() : combined.getStatefulSet().getSpec().getTemplate().getSpec());
            Container container = podSpec.getContainers().get(0);

            //Service
            getServices(request, combined, requestContainer);

            //Environments
            ArrayList<Link> links = new ArrayList<>();
            ArrayList<EnvironmentVariable> environmentVariables = new ArrayList<>();

            if (container.getEnv() != null) {
                for (EnvVar variable : container.getEnv()) {
                    if (variable.getValue() != null) {
                        KubernetesCombined linkCombined = searchLink(variable.getValue(), template.getCombinedList());

                        if (linkCombined == null) {
                            environmentVariables.add(new EnvironmentVariable(RandomWord.getRandomWordCombination(), variable.getName(), variable.getValue(), false));
                        } else {
                            links.add(createLink(variable.getName(), variable.getValue(), combined.getUniqueName(), linkCombined));
                        }
                    }
                }
            }

            for (ConfigMap configMap: combined.getConfigMapList()) {
                for (Map.Entry<String,String> entry: configMap.getData().entrySet()) {
                    KubernetesCombined linkCombined = searchLink(entry.getValue(), template.getCombinedList());

                    if (linkCombined == null) {
                        environmentVariables.add(new EnvironmentVariable(RandomWord.getRandomWordCombination(), entry.getKey(), entry.getValue(), false));
                    } else {
                        links.add(new Link(RandomWord.getRandomWordCombination(), entry.getKey(), combined.getUniqueName(), linkCombined.getUniqueName()));
                        links.add(createLink(entry.getKey(),entry.getValue(),combined.getUniqueName(),linkCombined));

                    }
                }
            }

            for (Secret secret: combined.getSecretList()) {
                for (Map.Entry<String, String> entry: secret.getData().entrySet()) {
                    String value = Base64Coder.decodeString(entry.getValue());
                    KubernetesCombined linkCombined = searchLink(value,template.getCombinedList());

                    if (linkCombined == null) {
                        environmentVariables.add(new EnvironmentVariable(RandomWord.getRandomWordCombination(), entry.getKey(), Base64Coder.decodeString(entry.getValue()), true));
                    } else {
                        links.add(createLink(entry.getKey(), value, combined.getUniqueName(), linkCombined));
                    }
                }
            }

            request.addAllLinks(links);
            requestContainer.setEnvironments(environmentVariables);

            //Probes
            if (container.getLivenessProbe() != null || container.getReadinessProbe() != null) {
                RequestProbe readinessProbe;
                RequestProbe livenessProbe;

                if (container.getReadinessProbe() != null) {
                    readinessProbe = getRequestProbe(requestContainer, container.getReadinessProbe());
                } else {
                    readinessProbe = new RequestProbe(1,0,10,1,1);
                }

                if (container.getLivenessProbe() != null) {
                    livenessProbe = getRequestProbe(requestContainer, container.getLivenessProbe());
                } else {
                    livenessProbe = new RequestProbe(1,0,10,1,1);
                }

                requestContainer.getContainerAdvancedOptions().setLivenessProbe(livenessProbe);
                requestContainer.getContainerAdvancedOptions().setReadinessProbe(readinessProbe);
            }

            //Resource Requirements
            if (container.getResources() != null) {
                createResourcesAndLimits(requestContainer, container);
            }

            if (podSpec.getInitContainers() != null && !podSpec.getInitContainers().isEmpty()) {
                getInitContainers(requestContainer, podSpec.getInitContainers());
            }

            if (container.getSecurityContext() != null && container.getSecurityContext().getPrivileged() != null) {
                requestContainer.getContainerAdvancedOptions().setPrivileged(container.getSecurityContext().getPrivileged());
            }
        }

        return request;
    }

    /**
     * Extracts the actual container from a Kubernetes Combined Object
     *
     * @param combined The KubernetesCombined Object from which to extract the actual container
     * @return The container in request form
     */
    private de.th.bingen.master.backend.model.request.container.Container getContainer(KubernetesCombined combined) {
        de.th.bingen.master.backend.model.request.container.Container requestContainer = new de.th.bingen.master.backend.model.request.container.Container();
        requestContainer.setTemplateName(combined.getTemplateName());
        PodSpec podSpec;
        Container container;
        Integer replicas;


        if (combined.getDeployment() != null) {
            Deployment deployment = combined.getDeployment();

            podSpec = deployment.getSpec().getTemplate().getSpec();
            container = podSpec.getContainers().get(0);
            replicas = deployment.getSpec().getReplicas();
        } else {
            StatefulSet statefulSet = combined.getStatefulSet();

            podSpec = statefulSet.getSpec().getTemplate().getSpec();
            container = podSpec.getContainers().get(0);
            replicas = statefulSet.getSpec().getReplicas();
            List<Storage> storage = getStorageFromStatefulSet(statefulSet);

            if (!storage.isEmpty()) {
                requestContainer.setStorage(storage);
            }
        }

        requestContainer.setUniqueName(combined.getUniqueName());
        requestContainer.setName(container.getName());
        requestContainer.setImage(container.getImage());
        requestContainer.setReplicas(replicas == null ? 1 : replicas);

        List<Port> ports = getPortsFromContainer(container);
        if (!ports.isEmpty()) {
            requestContainer.setPorts(ports);
        }

        return requestContainer;
    }

    /**
     * Extracts all initContainers from a PodSpec
     *
     * @param requestContainer The container for the request
     * @param initContainers The list of all initContainers from a Template PodSpec
     */
    private void getInitContainers(de.th.bingen.master.backend.model.request.container.Container requestContainer, List<Container> initContainers) {
        for (Container initContainer: initContainers) {
            InitContainer requestInitContainer = new InitContainer();

            requestInitContainer.setImage(initContainer.getImage());
            requestInitContainer.setName(initContainer.getName());

            if (initContainer.isPrivileged()) {
                requestInitContainer.setPrivileged(true);
            }

            StringBuilder command = new StringBuilder();
            for (String s: initContainer.getCommand()) {
                command.append(s);
                command.append(" ");
            }
            requestInitContainer.setCommand(command.toString().trim());

            if (initContainer.getVolumeMounts() != null && !initContainer.getVolumeMounts().isEmpty()) {
                requestInitContainer.setMountPath(initContainer.getVolumeMounts().get(0).getMountPath());
            }

            requestContainer.getContainerAdvancedOptions().getInitContainers().add(requestInitContainer);
        }
    }

    /**
     * Extracts the Liveness and Readiness Probes for a request from a Kubernetes Probe
     *
     * @param requestContainer The container for the request
     * @param containerProbe The actual Kubernetes Probe
     * @return The probe for the request
     */
    private RequestProbe getRequestProbe(de.th.bingen.master.backend.model.request.container.Container requestContainer, Probe containerProbe) {
        RequestProbe requestProbe;
        requestProbe = new RequestProbe(containerProbe.getFailureThreshold(), containerProbe.getInitialDelaySeconds(), containerProbe.getPeriodSeconds(), containerProbe.getSuccessThreshold(), containerProbe.getTimeoutSeconds());

        if (containerProbe.getTcpSocket() != null) {
            requestProbe.setTcpPort(containerProbe.getTcpSocket().getPort());
        }

        if (containerProbe.getHttpGet() != null) {
            requestProbe.setHttpAction(containerProbe.getHttpGet().getPath());
            requestProbe.setHttpPort(containerProbe.getHttpGet().getPort());
            requestContainer.getContainerAdvancedOptions().setHttpService(true);
        }

        return requestProbe;
    }

    /**
     * Creates the services for a container and adds them to the request
     *
     * @param request The Microservice request for which the container and services get created
     * @param combined The Kubernetes Combined object containing the deployment and all corresponding services
     * @param requestContainer The Kubernetes Container in request form
     */
    private void getServices(Request request, KubernetesCombined combined, de.th.bingen.master.backend.model.request.container.Container requestContainer) {
        for (de.th.bingen.master.backend.model.kubernetes.service.Service service: combined.getServiceList()) {
            if (service.getSpec().getType() != null && service.getSpec().getType() != ServiceType.ClusterIP) {
                de.th.bingen.master.backend.model.request.service.Service requestService = new de.th.bingen.master.backend.model.request.service.Service();
                requestService.setUniqueName(RandomWord.getRandomWordCombination());

                String serviceName = service.getMetadata().getName();
                requestService.setName(serviceName);
                requestService.setTypeAdvanced(service.getSpec().getType());
                requestService.getNormalServiceOptions().setContainerName(combined.getUniqueName());

                for (ServicePort port: service.getSpec().getPorts()) {
                    requestService.getNormalServiceOptions().addPort(port.getName(),port.getPort(),port.getTargetPort(),port.getProtocol());
                }

                request.addService(requestService);
            } else {
                for (ServicePort port: service.getSpec().getPorts()) {
                    boolean inserted = false;
                    if (port.getTargetPort() != null) {
                        for (Port requestPort : requestContainer.getPorts()) {
                            if (requestPort.getPort() == port.getTargetPort()) {
                                requestPort.setTargetPort(port.getTargetPort());
                                requestPort.setPort(port.getPort());
                                requestPort.setName(requestPort.getName() != null ? requestPort.getName() : port.getName());
                                inserted = true;
                            }
                        }
                    } else {
                        for (Port requestPort: requestContainer.getPorts()) {
                            if (requestPort.getPort() == port.getPort()) {
                                requestPort.setName(requestPort.getName() != null ? requestPort.getName() : port.getName());
                                inserted = true;
                            }
                        }
                    }

                    if (!inserted) {
                        Port requestPort = new Port();
                        requestPort.setUniqueName(RandomWord.getRandomWordCombination());
                        requestPort.setName(port.getName());
                        requestPort.setPort(port.getPort());
                        requestPort.setTargetPort(port.getTargetPort());
                        requestPort.setProtocol(port.getProtocol() != null ? port.getProtocol() : ProtocolType.TCP);
                    }


                }
            }
        }
    }

    /**
     * Creates Requests and Limits for a request container from a container
     *
     * @param requestContainer The request container
     * @param container The container from which the requirements should get extracted
     */
    private void createResourcesAndLimits(de.th.bingen.master.backend.model.request.container.Container requestContainer, Container container) {
        ResourceRequirements resources = container.getResources();
        RequestResource requestResource = new RequestResource();
        RequestResource limitResource = new RequestResource();

        if (resources.getRequests() != null) {
            extractResourceRequirement(requestResource, resources.getRequests());
        }

        if (resources.getLimits() != null) {
            extractResourceRequirement(limitResource, resources.getLimits());
        }

        requestContainer.getContainerAdvancedOptions().setRequests(requestResource);
        requestContainer.getContainerAdvancedOptions().setLimits(limitResource);
    }

    /**
     * Extracts resource requests and limits from a given requirement
     *
     * @param requestResource The request Resource which values should be replaced with values of the resource requirements object
     * @param resourceRequirements The resource requirements object from which the requests/limits should get extracted
     */
    private void extractResourceRequirement(RequestResource requestResource, ResourceRequirementsObject resourceRequirements) {
        String cpu = resourceRequirements.getCpu();
        if (cpu != null) {
            if (cpu.contains("m")) {
                cpu = cpu.substring(0, cpu.length() - 1);
                Double temp = Double.valueOf(cpu);
                cpu = String.valueOf(temp / 1000);
            }
            requestResource.setCpu(Double.valueOf(cpu));
        }

        String memory = resourceRequirements.getMemory();
        if (memory != null) {
            if (memory.contains("Gi")) {
                Double temp = (Double.parseDouble(memory.substring(0, memory.indexOf("Gi"))) * 1024);
                String tempString = String.valueOf(temp);
                Integer intTemp = Integer.valueOf(tempString.substring(0, tempString.indexOf(".")));
                memory = String.valueOf(intTemp);
            } else {
                memory = memory.substring(0, memory.indexOf("Mi"));
            }
            requestResource.setMemory(Integer.valueOf(memory));
        }
    }

    /**
     * Creates a Link for a request object.
     *
     * @param name The name of the environment variable
     * @param value The value of the environment variable
     * @param containerNameFrom The unique name of the container where the link should start
     * @param containerTo The Kubernetes combined object of the container where the link should end
     * @return The created Link
     */
    private Link createLink(String name, String value, String containerNameFrom, KubernetesCombined containerTo) {
        for (de.th.bingen.master.backend.model.kubernetes.service.Service service: containerTo.getServiceList()) {
            String serviceName = service.getMetadata().getName();

            if (value.equals(serviceName)) {
                return new Link(RandomWord.getRandomWordCombination(), name, containerNameFrom, containerTo.getUniqueName());
            }

            if (value.contains(serviceName)) {
                value = value.replaceAll(serviceName, "<link>");
            }
        }

        String containerName = (containerTo.getDeployment() != null ? containerTo.getDeployment().getMetadata().getName() : containerTo.getStatefulSet().getMetadata().getName());
        value = value.replaceAll(containerName,"<pod>");

        return new Link(RandomWord.getRandomWordCombination(), name, containerNameFrom, containerTo.getUniqueName(), value);
    }

    /**
     * Searches the services of a Kubernetes Combined List for Links between a name and a service
     *
     * @param name The name of the possible link
     * @param combinedList The List of Kubernetes Combined Objects
     * @return Either a Kubernetes Combined object, when a Link exist or null if no link exist
     */
    private KubernetesCombined searchLink(String name, List<KubernetesCombined> combinedList) {
        KubernetesCombined linkCombined = null;
        int matchLength = 0;

        for (KubernetesCombined combined: combinedList) {
            for (de.th.bingen.master.backend.model.kubernetes.service.Service service: combined.getServiceList()) {
                if (service.getMetadata().getName().equals(name)) {
                    return combined;
                }

                if (name.contains(service.getMetadata().getName()) && service.getMetadata().getName().length() > matchLength) {
                    matchLength = service.getMetadata().getName().length();
                    linkCombined = combined;
                }
            }
        }

        return linkCombined;
    }

    /**
     * Extracts the Container Ports from a Container and creates Ports for a request object
     *
     * @param container The container from which the ports should get extracted
     * @return A list of container ports in request object format
     */
    private List<Port> getPortsFromContainer(Container container) {
        List<Port> ports = new ArrayList<>();

        if (container.getPorts() == null) {
            return ports;
        }

        for (ContainerPort port: container.getPorts()) {
            ports.add(new Port(RandomWord.getRandomWordCombination(),port.getName(), port.getContainerPort(), port.getProtocol()));
        }

        return ports;
    }

    /**
     * Extracts the storage options from a statefulset and creates Storage objects for a request object
     *
     * @param statefulSet The statefulset from which the storages should get extracted
     * @return A list of storage in request object format
     */
    private List<Storage> getStorageFromStatefulSet(StatefulSet statefulSet) {
        List<Storage> storages = new ArrayList<>();

        StatefulSetSpec spec = statefulSet.getSpec();
        List<PersistentVolumeClaim> pvcs = spec.getVolumeClaimTemplates();
        List<VolumeMount> volumeMounts = spec.getTemplate().getSpec().getContainers().get(0).getVolumeMounts();

        if (pvcs == null || volumeMounts == null) {
            return storages;
        }

        for (PersistentVolumeClaim pvc: spec.getVolumeClaimTemplates()) {
            Storage storage = new Storage();
            storage.setName(pvc.getMetadata().getName());
            String size = pvc.getSpec().getResources().getRequests().getStorage();

            if (size.contains("Gi")) {
                size = size.substring(0, size.indexOf("Gi"));
                int temp = (int)(Double.parseDouble(size) * 1024);
                size = String.valueOf(temp);
            } else {
                size = size.substring(0, size.indexOf("Mi"));
            }

            storage.setSize(Integer.parseInt(size));

            for (VolumeMount volumeMount: volumeMounts) {
                if (volumeMount.getName().equals(pvc.getMetadata().getName())) {
                    storage.addStorageMount(volumeMount.getMountPath(),volumeMount.getSubPath());
                }
            }

            storages.add(storage);
        }

        return storages;
    }

    /**
     * Transforms the Template into a YAML file, which can be deployed in Kubernetes
     *
     * @param template The template which should be transformed into YAML
     * @return The template as YAML String
     * @throws IOException When something could not be serialized
     */
    private String templateToString(Template template) throws IOException {
        StringBuilder result = new StringBuilder();

        for (KubernetesCombined k: template.getCombinedList()) {
            result.append(generator.serializeToYAML(k));
        }

        return result.toString();
    }

    /**
     * Searches a template for a given name and returns a copy of it
     *
     * @param name The name of a template
     * @return A deep copy of the template
     * @throws IOException When the template could not be copied
     */
    private Template findTemplate(String name) throws IOException {
        List<Template> templates = templateLoader.getTemplates();

        for (int i = 0; i < templates.size(); i++) {
            if (templates.get(i).getName().equals(name)) {
                return templates.get(i).deepCopy();
            }
        }

        return null;
    }
}
