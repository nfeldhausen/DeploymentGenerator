package de.th.bingen.master.backend.services;

import de.th.bingen.master.backend.configuration.DatabaseConfiguration;
import de.th.bingen.master.backend.configuration.DatabaseConfigurations;
import de.th.bingen.master.backend.model.Template;
import de.th.bingen.master.backend.model.kubernetes.Ingress.HttpIngressRuleValue;
import de.th.bingen.master.backend.model.kubernetes.Ingress.Ingress;
import de.th.bingen.master.backend.model.kubernetes.Ingress.IngressRule;
import de.th.bingen.master.backend.model.kubernetes.Ingress.IngressSpec;
import de.th.bingen.master.backend.model.kubernetes.LabelSelector;
import de.th.bingen.master.backend.model.kubernetes.configMap.ConfigMap;
import de.th.bingen.master.backend.model.kubernetes.container.Capabilities;
import de.th.bingen.master.backend.model.kubernetes.container.ContainerPort;
import de.th.bingen.master.backend.model.kubernetes.container.SecurityContext;
import de.th.bingen.master.backend.model.kubernetes.container.VolumeMount;
import de.th.bingen.master.backend.model.kubernetes.container.lifecycle.Handler;
import de.th.bingen.master.backend.model.kubernetes.container.lifecycle.Lifecycle;
import de.th.bingen.master.backend.model.kubernetes.container.probe.Probe;
import de.th.bingen.master.backend.model.kubernetes.deployment.Deployment;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.ConfigMapEnvSource;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvFromSource;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.EnvVar;
import de.th.bingen.master.backend.model.kubernetes.environmentVariables.SecretEnvSource;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.PersistentVolumeClaim;
import de.th.bingen.master.backend.model.kubernetes.persistentVolumeClaim.ResourceRequirements;
import de.th.bingen.master.backend.model.kubernetes.pod.*;
import de.th.bingen.master.backend.model.kubernetes.secret.Secret;
import de.th.bingen.master.backend.model.kubernetes.service.ServicePort;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSet;
import de.th.bingen.master.backend.model.kubernetes.volumes.EmptyDirVolumeSource;
import de.th.bingen.master.backend.model.kubernetes.volumes.Volume;
import de.th.bingen.master.backend.model.request.container.*;
import de.th.bingen.master.backend.model.request.*;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;
import de.th.bingen.master.backend.model.request.enums.ServiceType;
import de.th.bingen.master.backend.model.request.service.HttpOption;
import de.th.bingen.master.backend.model.request.service.HttpPath;
import de.th.bingen.master.backend.model.request.service.IngressOptions;
import de.th.bingen.master.backend.model.request.service.TcpUdpOption;
import de.th.bingen.master.backend.model.response.KubernetesCombined;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class Generator {
    @Autowired
    private YAMLGenerator yamlGenerator;
    @Autowired
    private DatabaseConfigurations databaseConfigurations;
    @Autowired
    private KubernetesYAMLService kubernetesYAMLService;
    @Autowired
    private TemplateService templateService;

    /**
     * Creates the different deployments, databases, services and links of the request
     *
     * @param request The request which should be transformed into deployable "Kubernetes YAML"
     * @return A list with an object consisting of Deployment (or Statefuleset), their services and Links
     */
    public List<KubernetesCombined> generateDistinctFiles(Request request) {
        if ((request.getContainers() == null || request.getContainers().isEmpty()) && (request.getDatabases() == null || request.getDatabases().isEmpty())) {
            return new ArrayList<>();
        }

        ArrayList<KubernetesCombined> kubernetesCombineds = new ArrayList<>();

        //Creates the containers for kubernetes (one per kubernetes combined)
        for (Container c: request.getContainers()) {
            if (c.getStorage() != null && !c.getStorage().isEmpty()) {
                kubernetesCombineds.add(createStatefulSet(c));
            } else {
                kubernetesCombineds.add(createDeployment(c));
            }
        }

        //Creates the Databases for the request
        for (Database database: request.getDatabases()) {
            DatabaseConfiguration dbConfig = getDatabaseConfiguration(database.getImage());

            if (dbConfig != null) {
                kubernetesCombineds.add(createStatefulSet(database,dbConfig));
            }
        }

        //Creates the Services or Ingress
        for (de.th.bingen.master.backend.model.request.service.Service service: request.getServices()) {
            if (service.getType() != ServiceType.Ingress) {
                KubernetesCombined containerResult = findKubernetesCombined(kubernetesCombineds, service.getNormalServiceOptions().getContainerName());

                if (containerResult != null) {
                    createService(containerResult, service);
                }
            } else {
                kubernetesCombineds.add(createIngress(service, kubernetesCombineds));
            }
        }

        //Creates links
        for (Link link: request.getLinks()) {
            KubernetesCombined combinedFrom = findKubernetesCombined(kubernetesCombineds, link.getContainerFrom());
            KubernetesCombined combinedTo = findKubernetesCombined(kubernetesCombineds, link.getContainerTo());

            if (combinedFrom != null && combinedTo != null) {
                createLink(link, combinedFrom, combinedTo);
            }
        }

        //Checks if the request is from a template and depending on that sets template defaults
        if (request.getTemplateName() != null && !request.getTemplateName().equals("")) {
            List<Template> templates = templateService.getTemplates();
            Template template = null;

            for (int i = 0; i < templates.size() && template == null; i++) {
                if (templates.get(i).getName().equals(request.getTemplateName())) {
                    template = templates.get(i);
                }
            }
            
            if (template != null) {
                setTemplateDefaults(template, kubernetesCombineds);
            }
        }

        return kubernetesCombineds;
    }

    /**
     * Creates a StatefulSet for a container, which needs storage or persistent names
     *
     * @param container The requested container
     * @return The Kubernetes Combined object containing a statefulset
     */
    public KubernetesCombined createStatefulSet(Container container) {
        KubernetesCombined combined = new KubernetesCombined();
        combined.setTemplateName(container.getTemplateName());
        combined.setUniqueName(container.getUniqueName());

        String name = container.getName();
        LabelSelector selector = createSelector(container.getName(), container.getUniqueName());
        List<ContainerPort> containerPorts = getContainerPorts(container.getPorts());

        String serviceName = null;
        if (container.getPorts() != null && !container.getPorts().isEmpty()) {
            serviceName = name + "-service-" + container.getUniqueName();

            List<ServicePort> servicePorts = getServicePorts(container.getPorts());
            combined.addService(kubernetesYAMLService.getService(serviceName, selector.getMatchLabels(), ServiceType.ClusterIP, null, servicePorts));

            if (container.isExpose()) {
                combined.addService(kubernetesYAMLService.getService(name + "-externalservice-" + container.getUniqueName(), selector.getMatchLabels(), container.getExposeType(), null, servicePorts));
            }
        }

        StatefulSet statefulSet = kubernetesYAMLService.getStatefulSet(name + "-" + container.getUniqueName(),selector.getMatchLabels(),container.getReplicas(),selector,serviceName,container.getName(),container.getImage(),null,containerPorts);

        List<Storage> storageList = container.getStorage();
        for (int i = 0; i < storageList.size(); i++) {
            String pvcName = (storageList.get(i).getName() != null && !storageList.get(i).getName().trim().equals("") ? storageList.get(i).getName() + "-" + container.getUniqueName() : name + "-data" + i + "-" + container.getUniqueName());
            PersistentVolumeClaim pvc = kubernetesYAMLService.getPersistentVolumeClaim(pvcName, storageList.get(i).getSize(), selector.getMatchLabels());
            statefulSet.addPersistentVolumeClaimTemplate(pvc);

            for (StorageMount mount: storageList.get(i).getStorageMounts()) {
                if (mount.getSubPath() != null && mount.getSubPath().trim().equals("")) {
                    mount.setSubPath(null);
                }

                statefulSet.addVolumeMount(pvcName, mount.getMountPath(), mount.getSubPath());
            }
        }

        combined.setStatefulSet(statefulSet);
        createEnvFrom(container.getEnvironments(), combined);

        createProbes(statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0), container.getContainerAdvancedOptions());
        createReqLim(statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0), container.getContainerAdvancedOptions());
        createInitContainers(statefulSet.getSpec().getTemplate().getSpec(), container.getContainerAdvancedOptions().getInitContainers());

        if (container.getContainerAdvancedOptions().getPrivileged() != null && container.getContainerAdvancedOptions().getPrivileged()) {
            statefulSet.getSpec().getTemplate().getSpec().getContainers().get(0).setSecurityContext(new SecurityContext(true));
        }

        return combined;
    }

    /**
     * Creates a kubernetes container for the requested container
     *
     * @param container The requested container
     * @return A Kubernetes Combined Object with a deployment
     */
    private KubernetesCombined createDeployment(Container container) {
        KubernetesCombined containerResult = new KubernetesCombined();
        containerResult.setTemplateName(container.getTemplateName());
        containerResult.setUniqueName(container.getUniqueName());

        String name = container.getName();
        LabelSelector selector = createSelector(container.getName(), container.getUniqueName());
        List<ContainerPort> containerPorts = getContainerPorts(container.getPorts());

        Deployment deployment = kubernetesYAMLService.getDeployment(name + "-" + container.getUniqueName(), selector.getMatchLabels(), container.getReplicas(), selector, container.getName(), container.getImage(), null, containerPorts);
        containerResult.setDeployment(deployment);
        createEnvFrom(container.getEnvironments(), containerResult);

        List<ServicePort> servicePorts = getServicePorts(container.getPorts());

        if (servicePorts != null) {
            containerResult.getServiceList().add(kubernetesYAMLService.getService(name + "-service-" + container.getUniqueName(),selector.getMatchLabels(),ServiceType.ClusterIP, null,servicePorts));

            if (container.isExpose()) {
                containerResult.getServiceList().add(kubernetesYAMLService.getService(name + "-externalservice-" + container.getUniqueName(), selector.getMatchLabels(),container.getExposeType(),null,servicePorts));
            }
        }

        createProbes(deployment.getSpec().getTemplate().getSpec().getContainers().get(0), container.getContainerAdvancedOptions());
        createReqLim(deployment.getSpec().getTemplate().getSpec().getContainers().get(0), container.getContainerAdvancedOptions());
        createInitContainers(deployment.getSpec().getTemplate().getSpec(), container.getContainerAdvancedOptions().getInitContainers());
        deployment.getSpec().getTemplate().getSpec().setTerminationGracePeriodSeconds(container.getContainerAdvancedOptions().getGracePeriod());

        if (container.getContainerAdvancedOptions().getPrivileged() != null && container.getContainerAdvancedOptions().getPrivileged()) {
            deployment.getSpec().getTemplate().getSpec().getContainers().get(0).setSecurityContext(new SecurityContext(true));
        }

        return containerResult;
    }

    /**
     * Creates a statefulset for a requested database
     *
     * @param database The requested database
     * @param dbConfig The config of the database (with ports and required environment variables
     * @return A Kubernetes Combined Object with a stateful set
     */
    private KubernetesCombined createStatefulSet(Database database, DatabaseConfiguration dbConfig) {
        KubernetesCombined databaseResult = new KubernetesCombined();
        databaseResult.setUniqueName(database.getUniqueName());

        String name = database.getName();
        LabelSelector selector = createSelector(database.getName(), database.getUniqueName());
        List<ContainerPort> containerPorts = Arrays.asList(new ContainerPort(dbConfig.getPort(), dbConfig.getName(), ProtocolType.TCP));

        for (EnvironmentVariable variable: database.getEnvironments()) {
            if (variable.getName() != null) {
                if (dbConfig.getMustSetVariables().contains(variable.getName()) && (variable.getValue() == null || variable.getValue().trim().equals(""))) {
                    variable.setValue(RandomStringUtils.randomAlphanumeric(8));

                    if (variable.isSecret()) {
                        variable.setValue(Base64.getEncoder().encodeToString(variable.getValue().getBytes()));
                    }
                }
            }
        }

        StatefulSet statefulSet = kubernetesYAMLService.getStatefulSet(name + "-" + database.getUniqueName(), selector.getMatchLabels(),1, selector, name + "-service-" + database.getUniqueName(), database.getImage(),dbConfig.getImage(),null,containerPorts);

        PersistentVolumeClaim persistentVolumeClaim = kubernetesYAMLService.getPersistentVolumeClaim(name + "-data-" + database.getUniqueName(), database.getSize(),selector.getMatchLabels());
        statefulSet.addPersistentVolumeClaimTemplate(persistentVolumeClaim);
        statefulSet.addVolumeMount(name + "-data-" + database.getUniqueName(), dbConfig.getMountPath(), "data");

        databaseResult.setStatefulSet(statefulSet);
        createEnvFrom(database.getEnvironments(), databaseResult);

        List<ServicePort> servicePorts = Arrays.asList(new ServicePort(dbConfig.getName(), dbConfig.getPort(), dbConfig.getPort(), ProtocolType.TCP));

        databaseResult.getServiceList().add(kubernetesYAMLService.getService(name + "-service-" + database.getUniqueName(), selector.getMatchLabels(), ServiceType.ClusterIP, null, servicePorts));

        if (database.isExpose()) {
            databaseResult.getServiceList().add(kubernetesYAMLService.getService(name + "-externalservice-" + database.getUniqueName(), selector.getMatchLabels(), database.getType(), null, servicePorts));
        }

        return databaseResult;
    }

    /**
     * Creates InitContainers from the request.
     *
     * @param spec The PodSpec for which initContainers should get created
     * @param initContainers A list of requested initContainers
     */
    private void createInitContainers(PodSpec spec, List<InitContainer> initContainers) {
        if (initContainers.isEmpty()) {
            return;
        }

        List<de.th.bingen.master.backend.model.kubernetes.container.Container> resultInitContainers = new ArrayList<>();
        int containerIndex = 0;

        for (InitContainer initContainer: initContainers) {
            de.th.bingen.master.backend.model.kubernetes.container.Container container  = new de.th.bingen.master.backend.model.kubernetes.container.Container();

            container.setName(initContainer.getName() != null ? initContainer.getName() : "init" + containerIndex);
            container.setImage(initContainer.getImage());
            container.setEnvFrom(spec.getContainers().get(0).getEnvFrom());

            List<String> commands = new ArrayList<>();
            String[] splitCommands = initContainer.getCommand().split(" ");

            if (splitCommands.length < 3) {
                commands.add(splitCommands[0]);
                if (1 < splitCommands.length) {
                    commands.add(splitCommands[1]);
                }
            } else {
                commands.add(splitCommands[0]);
                commands.add(splitCommands[1]);

                StringBuilder commandString = new StringBuilder();
                for (int i = 2; i < splitCommands.length; i++) {
                    commandString.append(splitCommands[i]);
                    commandString.append(" ");
                }

                commands.add(commandString.toString().trim());
            }

            container.setCommand(commands);

            if (initContainer.isPrivileged()) {
                container.setSecurityContext(new SecurityContext(true));
            }

            if (initContainer.getMountPath() != null && initContainer.getMountPath().matches("\\/([a-z]|[A-Z]|[0-9]|\\/)+")) {
                VolumeMount mount = null;

                if (spec.getContainers().get(0).getVolumeMounts() != null) {
                    for (VolumeMount volumeMount: spec.getContainers().get(0).getVolumeMounts()) {
                        if (volumeMount.getMountPath().equals(initContainer.getMountPath())) {
                            mount = volumeMount;
                        }
                    }
                }

                if (mount != null) {
                    container.addVolumeMount(mount);
                } else {
                    Volume volume = new Volume("init" + containerIndex, new EmptyDirVolumeSource());
                    mount = new VolumeMount(initContainer.getMountPath(),"init" + containerIndex, null);

                    container.addVolumeMount(mount);
                    spec.getContainers().get(0).addVolumeMount(mount);
                    spec.addVolume(volume);
                }
            }

            containerIndex++;
            resultInitContainers.add(container);
        }

        spec.setInitContainers(resultInitContainers);
    }

    /**
     * Creates an Ingress Service for Kubernetes
     *
     * @param service The requested service
     * @param result Already created list of deployments and stateful sets
     * @return Object consisting of Ingress Pod, Ingress Service and Ingress Config Maps
     */
    private KubernetesCombined createIngress(de.th.bingen.master.backend.model.request.service.Service service, ArrayList<KubernetesCombined> result) {
        KubernetesCombined resultCombined = new KubernetesCombined();

        IngressOptions ingressOptions = service.getIngressOptions();
        LabelSelector selector = createSelector(service.getName(), "ingress-" + service.getUniqueName());
        List<Port> requestPorts = createIngressConfigMaps(resultCombined,service,result);

        resultCombined.addService(kubernetesYAMLService.getService(service.getName() + "-ingress-service-" + service.getUniqueName(), selector.getMatchLabels(), ingressOptions.getServiceType(), "None", getServicePorts(requestPorts)));

        List<EnvVar> envVars = new ArrayList<>();
        EnvVar envVar1 = new EnvVar("POD_NAME");
        envVar1.setValueFromFieldRef("metadata.name");
        envVars.add(envVar1);
        EnvVar envVar2 = new EnvVar("POD_NAMESPACE");
        envVar2.setValueFromFieldRef("metadata.namespace");
        envVars.add(envVar2);

        Deployment ingressDeployment = kubernetesYAMLService.getDeployment(service.getName() + "-ingress-controller-" + service.getUniqueName(),selector.getMatchLabels(),1,selector,"nginx-ingress-controller","quay.io/kubernetes-ingress-controller/nginx-ingress-controller:0.26.1",envVars, getContainerPorts(requestPorts));
        PodSpec ingressPodSpec = ingressDeployment.getSpec().getTemplate().getSpec();
        ingressPodSpec.setTerminationGracePeriodSeconds(300);
        ingressPodSpec.setServiceAccountName("nginx-ingress-serviceaccount");

        de.th.bingen.master.backend.model.kubernetes.container.Container ingressContainer = ingressPodSpec.getContainers().get(0);
        ingressContainer.addArg("/nginx-ingress-controller");
        ingressContainer.addArg("--configmap=$(POD_NAMESPACE)/" + service.getName() + "-configuration-" + service.getUniqueName());
        ingressContainer.addArg("--tcp-services-configmap=$(POD_NAMESPACE)/" + service.getName() + "-tcp-services-" + service.getUniqueName());
        ingressContainer.addArg("--udp-services-configmap=$(POD_NAMESPACE)/" + service.getName() + "-udp-services-" + service.getUniqueName());
        ingressContainer.addArg("--publish-service=$(POD_NAMESPACE)/" + service.getName() + "-ingress-service-" + service.getUniqueName());
        ingressContainer.addArg("--ingress-class=" + service.getName() + "-" + service.getUniqueName());

        SecurityContext securityContext = new SecurityContext();
        securityContext.setAllowPrivilegeEscalation(true);
        securityContext.setRunAsUser(33);
        Capabilities capabilities = new Capabilities();
        capabilities.addAdd("NET_BIND_SERVICE");
        capabilities.addDrop("ALL");
        securityContext.setCapabilities(capabilities);
        ingressContainer.setSecurityContext(securityContext);

        ingressContainer.setLivenessProbe(new Probe(3,10,10,1,10,"/healthz",10254));
        ingressContainer.setReadinessProbe(new Probe(3,1,10,1,10,"/healthz",10254));

        ingressContainer.setLifecycle(new Lifecycle(null,new Handler("/wait-shutdown")));
        resultCombined.setDeployment(ingressDeployment);

        if (!ingressOptions.getHttpOptions().isEmpty()) {
            Ingress ingress = new Ingress(service.getName() + "-ingress-" + service.getUniqueName(), selector.getMatchLabels());
            List<IngressRule> ingressRules = new ArrayList<>();


            for (HttpOption option : ingressOptions.getHttpOptions()) {
                IngressRule rule = new IngressRule();
                rule.setHost(option.getHost());
                HttpIngressRuleValue value = new HttpIngressRuleValue();


                for (HttpPath path : option.getPaths()) {
                    KubernetesCombined temp = findKubernetesCombined(result, path.getContainer());
                    String containerName = (temp.getDeployment() != null ? temp.getDeployment().getMetadata().getName() : temp.getStatefulSet().getMetadata().getName());
                    String name = containerName.substring(0,containerName.indexOf(temp.getUniqueName()) -1 ) + "-service-" + temp.getUniqueName();

                    value.addPath(name, (path.getPort() != null ? path.getPort() : 80), path.getPath());
                }

                rule.setHttp(value);
                ingressRules.add(rule);
            }
            ingress.setSpec(new IngressSpec(ingressRules));
            ingress.getMetadata().addAnnotation("kubernetes.io/ingress.class", service.getName() + "-" + service.getUniqueName());

            if (ingressOptions.isAffinity()) {
                ingress.getMetadata().addAnnotation("nginx.ingress.kubernetes.io/affinity","cookie");
            }

            resultCombined.setIngress(ingress);
        }

        return resultCombined;
    }

    /**
     * Creates the necessary ConfigMaps for the Ingress service type and returns corresponding ports of tcp and udp ports
     *
     * @param resultCombined The Kubernetes Combined Object of the Ingress Service
     * @param service The requested Ingress service
     * @param result Already created list of deployments and stateful sets
     * @return Port list of the ingress
     */
    public List<Port> createIngressConfigMaps(KubernetesCombined resultCombined, de.th.bingen.master.backend.model.request.service.Service service, ArrayList<KubernetesCombined> result) {
        ArrayList<Port> requestPorts = new ArrayList<>();
        LabelSelector selector = createSelector(service.getName(), "ingress-" + service.getUniqueName());
        IngressOptions ingressOptions = service.getIngressOptions();

        requestPorts.add(new Port("http","http",80, ProtocolType.TCP));
        requestPorts.add(new Port("https","https",443, ProtocolType.TCP));

        resultCombined.addConfigMap(new ConfigMap(service.getName() + "-configuration-" + service.getUniqueName(), selector.getMatchLabels()));
        ConfigMap tcpServices = new ConfigMap(service.getName() + "-tcp-services-" + service.getUniqueName(), selector.getMatchLabels());
        ConfigMap udpServices = new ConfigMap(service.getName() + "-udp-services-" + service.getUniqueName(), selector.getMatchLabels());

        for (TcpUdpOption option: ingressOptions.getTcpUdpOptions()) {
            KubernetesCombined temp = findKubernetesCombined(result, option.getContainerName());
            String containerName = (temp.getDeployment() != null ? temp.getDeployment().getMetadata().getName() : temp.getStatefulSet().getMetadata().getName());
            String serviceName = containerName.substring(0,containerName.indexOf(temp.getUniqueName()) -1 ) + "-service-" + temp.getUniqueName();

            Port port = option.getPort();
            if (port.getProtocol() == ProtocolType.TCP) {
                tcpServices.addData(port.getPort() + "", "default/" + serviceName + ":" + port.getPort());
            } else {
                udpServices.addData(port.getPort() + "", "default/" + serviceName + ":" + port.getPort());
            }

            if (option.getPort().getPort() != 80 && option.getPort().getPort() != 443) {
                port.setName(port.getName() + "-" + temp.getUniqueName());
                port.setName(port.getName().substring(0, Math.min(15,port.getName().length())));
                requestPorts.add(port);
            }
        }

        resultCombined.addConfigMap(tcpServices);
        resultCombined.addConfigMap(udpServices);

        return requestPorts;
    }

    /**
     * Sets some default settings for a container, which a user can not set from outside
     *
     * @param template The corresponding template file for the request
     * @param result The result of the request as Kubernetes Combined objects
     */
    private void setTemplateDefaults(Template template, ArrayList<KubernetesCombined> result) {
        for (KubernetesCombined combined: result) {
            if (combined.getTemplateName() != null && !combined.getTemplateName().equals("")) {
                List<KubernetesCombined> templateCombinedList = template.getCombinedList();
                KubernetesCombined templateCombined = null;

                for (int i = 0; i < templateCombinedList.size() && templateCombined == null; i++) {
                    if (combined.getTemplateName().equals(templateCombinedList.get(i).getTemplateName())) {
                        templateCombined = templateCombinedList.get(i);
                    }
                }

                if (templateCombined != null) {
                    PodSpec spec = (combined.getDeployment() != null ? combined.getDeployment().getSpec().getTemplate().getSpec() : combined.getStatefulSet().getSpec().getTemplate().getSpec());
                    PodSpec templateSpec = (templateCombined.getDeployment() != null ? templateCombined.getDeployment().getSpec().getTemplate().getSpec() : templateCombined.getStatefulSet().getSpec().getTemplate().getSpec());
                    de.th.bingen.master.backend.model.kubernetes.container.Container container = spec.getContainers().get(0);
                    de.th.bingen.master.backend.model.kubernetes.container.Container templateContainer = templateSpec.getContainers().get(0);

                    spec.setServiceAccountName(templateSpec.getServiceAccountName());
                    spec.setTerminationGracePeriodSeconds(templateSpec.getTerminationGracePeriodSeconds());
                    spec.setAffinity(templateSpec.getAffinity());
                    container.setLifecycle(templateContainer.getLifecycle());
                    container.setImagePullPolicy(templateContainer.getImagePullPolicy());

                    if (templateContainer.getEnv() != null) {
                        for (EnvVar variable: templateContainer.getEnv()) {
                            if (variable.getValueFrom() != null && (variable.getValueFrom().getFieldRef() != null || variable.getValueFrom().getResourceFieldRef() != null)) {
                                container.addEnv(variable);
                            }
                        }
                    }

                    if (container.getReadinessProbe() == null) {
                        container.setReadinessProbe(templateContainer.getReadinessProbe());
                    }

                    if (container.getLivenessProbe() == null) {
                        container.setLivenessProbe(templateContainer.getLivenessProbe());
                    }

                    if (container.getSecurityContext() != null) {
                        Boolean privileged = container.getSecurityContext().getPrivileged();
                        container.setSecurityContext(templateContainer.getSecurityContext());
                        container.getSecurityContext().setPrivileged(privileged);
                    }
                }
            }
        }
    }

    /**
     * Creates a Kubernetes Selector
     *
     * @param image The name of the deployed app
     * @param uniqueName The unique name of the Kubernetes Combined Object
     * @return A LabelSelector consisting of app and identifier
     */
    private LabelSelector createSelector(String image, String uniqueName) {
        HashMap<String, String> labels = new HashMap<>();
        labels.put("app",image);
        labels.put("ident",uniqueName);

        return new LabelSelector(labels);
    }

    /**
     * Creates a service for a kubernetesCombined object
     *
     * @param kubernetesCombined The object for which deployment a service should be created
     * @param service The requested service for the deployment
     * @return The updated Kubernetes Combind Object (updated the services)
     */
    private void createService(KubernetesCombined kubernetesCombined, de.th.bingen.master.backend.model.request.service.Service service) {
        String name = service.getName() + "-" + service.getUniqueName();
        HashMap<String, String> labels = (kubernetesCombined.getDeployment() != null ? kubernetesCombined.getDeployment() : kubernetesCombined.getStatefulSet()).getMetadata().getLabels();
        List<ServicePort> servicePorts = getServicePorts(service.getNormalServiceOptions().getPorts());
        String sessionAffinity = null;

        if (service.getNormalServiceOptions().isSessionAffinity()) {
            sessionAffinity = "ClientIP";
        }

        kubernetesCombined.getServiceList().add(kubernetesYAMLService.getService(name, labels, service.getType(), sessionAffinity, servicePorts));
    }

    /**
     * Creates ConfigMaps and Secrets for Environment Variables
     *
     * @param variables List of requested environment variables
     * @param kubernetesCombined The Kubernetes Combined object containing a deployment or statefulset
     */
    public void createEnvFrom(List<EnvironmentVariable> variables, KubernetesCombined kubernetesCombined) {
        de.th.bingen.master.backend.model.kubernetes.container.Container container = null;
        String name = null;
        HashMap<String, String> labels = null;

        if (kubernetesCombined.getDeployment() != null) {
            container = kubernetesCombined.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0);
            name = kubernetesCombined.getDeployment().getMetadata().getName();
            labels = kubernetesCombined.getDeployment().getMetadata().getLabels();
        } else {
            container = kubernetesCombined.getStatefulSet().getSpec().getTemplate().getSpec().getContainers().get(0);
            name = kubernetesCombined.getStatefulSet().getMetadata().getName();
            labels = kubernetesCombined.getStatefulSet().getMetadata().getLabels();
        }

        ConfigMap environments = new ConfigMap(name + "-environment",labels);
        Secret secret = new Secret(name + "-secret", labels);

        for (EnvironmentVariable variable: variables) {
            if (variable.getValue() == null) {
                variable.setValue("");
            }

            if (variable.isSecret()) {
                secret.addData(variable.getName(), variable.getValue());
            } else {
                environments.addData(variable.getName(), variable.getValue());
            }
        }

        if (environments.getData() != null && !environments.getData().isEmpty()) {
            container.addEnvFrom(new EnvFromSource(new ConfigMapEnvSource(name + "-environment")));
            kubernetesCombined.addConfigMap(environments);
        }

        if (secret.getData() != null && !secret.getData().isEmpty()) {
            container.addEnvFrom(new EnvFromSource(new SecretEnvSource(name + "-secret")));
            kubernetesCombined.addSecret(secret);
        }
    }

    /**
     * Creates a link from a container to another container in form of an environment variable
     *
     * @param link The requested link with the name of the environment variable
     * @param combinedFrom The deployment from which a link should be created
     * @param combinedTo The deployment to which a link should be createad
     * @return The updated Kubernetes Combined Object (updated the deployment)
     */
    private KubernetesCombined createLink(Link link, KubernetesCombined combinedFrom, KubernetesCombined combinedTo) {
        de.th.bingen.master.backend.model.kubernetes.container.Container containerFrom = null;

        PodSpec podSpec = (combinedFrom.getDeployment() != null ? combinedFrom.getDeployment().getSpec().getTemplate().getSpec() : combinedFrom.getStatefulSet().getSpec().getTemplate().getSpec());
        containerFrom = podSpec.getContainers().get(0);

        if (containerFrom != null) {
            List<EnvVar> envList = containerFrom.getEnv();

            if (envList == null) {
                envList = new ArrayList<>();
                containerFrom.setEnv(envList);
            }

            if (!doesEnvVarExist(envList, link.getName())) {
                String containerToName = (combinedTo.getDeployment() != null ? combinedTo.getDeployment().getMetadata().getName() : combinedTo.getStatefulSet().getMetadata().getName());
                String serviceName = containerToName.substring(0,containerToName.indexOf(combinedTo.getUniqueName()) - 1) + "-service-" + combinedTo.getUniqueName();

                EnvVar var;

                if (link.getScheme() != null && link.getScheme().contains("<link>")) {
                    link.setScheme(link.getScheme().replaceAll("<link>",serviceName));
                    link.setScheme(link.getScheme().replaceAll("<pod>",(combinedTo.getDeployment() != null ? combinedTo.getDeployment().getMetadata().getName() : combinedTo.getStatefulSet().getMetadata().getName())));
                    var = new EnvVar(link.getName(), link.getScheme());
                } else {
                    var = new EnvVar(link.getName(),serviceName);
                }

                envList.add(var);

                if (podSpec.getInitContainers() != null) {
                    for (de.th.bingen.master.backend.model.kubernetes.container.Container initContainers: podSpec.getInitContainers()) {
                        initContainers.setEnv(envList);
                    }
                }
            }
        }

        return combinedFrom;
    }

    /**
     * Checks if a environment variable in a given environment variable list exists
     *
     * @param list The environment variable list
     * @param name The name of the environment variable to check if it exists
     * @return true if the environment variable exists in the list
     */
    private boolean doesEnvVarExist(List<EnvVar> list, String name) {
        for (EnvVar var: list) {
            if (var.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates and sets the Requests and Limits for a container
     *
     * @param container The Container, the requests and limits should apply to
     * @param containerAdvancedOptions The advanced options of the container with the limits and requests
     */
    private void createReqLim(de.th.bingen.master.backend.model.kubernetes.container.Container container, ContainerAdvancedOptions containerAdvancedOptions) {
        RequestResource request = containerAdvancedOptions.getRequests();
        RequestResource limits = containerAdvancedOptions.getLimits();

        Double cpuRequest = null;
        Integer memoryRequest = null;
        Double cpuLimit = null;
        Integer memoryLimit = null;

        if (request != null) {
            cpuRequest = request.getCpu();
            memoryRequest = request.getMemory();
        }

        if (limits != null) {
            cpuLimit = limits.getCpu();
            memoryLimit = limits.getMemory();
        }

        if (cpuRequest != null || memoryRequest != null || cpuLimit != null || memoryLimit != null) {
            if (cpuRequest != null && cpuLimit != null) {
                cpuLimit = (cpuRequest > cpuLimit ? cpuRequest : cpuLimit);
            }

            if (memoryRequest != null && memoryLimit != null) {
                memoryLimit = (memoryRequest > memoryLimit ? memoryRequest : memoryLimit);
            }

            ResourceRequirements req = new ResourceRequirements();

            if (cpuRequest != null || memoryRequest != null) {
                req.setRequests(cpuRequest, memoryRequest);
            }

            if (cpuLimit != null || memoryLimit != null) {
                req.setLimits(cpuLimit, memoryLimit);
            }

            container.setResources(req);
        }
    }

    /**
     * Creates Liveness and Readniess Probes depending on the request
     *
     * @param container The container which should get liveness and readiness probes
     * @param containerAdvancedOptions The request advanced options including the liveness and readiness requests
     */
    public void createProbes(de.th.bingen.master.backend.model.kubernetes.container.Container container, ContainerAdvancedOptions containerAdvancedOptions) {
        if (containerAdvancedOptions.getReadinessProbe() != null && (containerAdvancedOptions.getReadinessProbe().getTcpPort() != null || (containerAdvancedOptions.getReadinessProbe().getHttpAction() != null && !containerAdvancedOptions.getReadinessProbe().getHttpAction().trim().equals("")))) {
            RequestProbe readinessProbe = containerAdvancedOptions.getReadinessProbe();

            Probe probe = getProbe(containerAdvancedOptions, readinessProbe);

            container.setReadinessProbe(probe);
            container.setLivenessProbe(probe);
        }

        if (containerAdvancedOptions.getLivenessProbe() == null) {
            return;
        }

        if (containerAdvancedOptions.getLivenessProbe().getTcpPort() != null || (containerAdvancedOptions.getLivenessProbe().getHttpAction() != null && !containerAdvancedOptions.getLivenessProbe().getHttpAction().trim().equals(""))) {
            RequestProbe livenessProbe = containerAdvancedOptions.getLivenessProbe();
            container.setLivenessProbe(getProbe(containerAdvancedOptions, livenessProbe));
        }
    }

    /**
     * Creates a single probe
     *
     * @param containerAdvancedOptions The request advanced options identifying the probe as http service or not
     * @param requestProbe The requested probe
     * @return The Probe for a Kubernetes Container
     */
    private Probe getProbe(ContainerAdvancedOptions containerAdvancedOptions, RequestProbe requestProbe) {
        Probe probe = null;

        if (containerAdvancedOptions.isHttpService() && requestProbe.getHttpAction() != null && !requestProbe.getHttpAction().trim().equals("")) {
            probe = new Probe(requestProbe.getFailureThreshold(), requestProbe.getInitialDelaySeconds(), requestProbe.getPeriodSeconds(), requestProbe.getSuccessThreshold(), requestProbe.getTimeoutSeconds(), requestProbe.getHttpAction(), requestProbe.getHttpPort());
        }

        if (!containerAdvancedOptions.isHttpService() && requestProbe.getTcpPort() != null) {
            probe = new Probe(requestProbe.getFailureThreshold(), requestProbe.getInitialDelaySeconds(), requestProbe.getPeriodSeconds(), requestProbe.getSuccessThreshold(), requestProbe.getTimeoutSeconds(), requestProbe.getTcpPort());
        }

        return probe;
    }

    /**
     * Find a Kubernetes Combined Object for a given name (The uniqueName of the object)
     *
     * @param list The list of Kubernetes Combined Objects
     * @param name The unique name of the object to find
     * @return The Kubernetes Combined Object if it exists or null otherwise
     */
    private KubernetesCombined findKubernetesCombined(List<KubernetesCombined> list, String name) {
        for (KubernetesCombined k: list) {
            if (k.getUniqueName().equals(name)) {
                return k;
            }
        }

        return null;
    }

    /**
     * Transforms ports of a requested service into the service port format of Kubernetes
     *
     * @param ports The list which to transform
     * @return The port list in serivce format for Kubernetes
     */
    private List<ServicePort> getServicePorts(List<Port> ports) {
        if (ports.isEmpty()) {
            return null;
        }

        ArrayList<ServicePort> servicePorts = new ArrayList<>();
        int index = 0;

        for (Port port: ports) {
            ServicePort servicePort = new ServicePort();

            if (port.getName() == null || port.getName().trim().equals("")) {
                servicePort.setName("port" + index++);
            } else {
                servicePort.setName(port.getName());
            }

            servicePort.setPort(port.getPort());
            servicePort.setTargetPort(port.getTargetPort());
            servicePort.setProtocol(port.getProtocol());

            servicePorts.add(servicePort);
        }

        return servicePorts;
    }

    /**
     * Transforms ports of a requested container into the container port format of Kubernetes
     *
     * @param ports The list which to transform
     * @return The port list in container format for Kubernetes
     */
    private List<ContainerPort> getContainerPorts(List<Port> ports) {
        ArrayList<ContainerPort> result = new ArrayList<>();
        int index = 0;

        for (Port port: ports) {
            ContainerPort containerPort = new ContainerPort();

            if (port.getName() == null || port.getName().trim().equals("")) {
                containerPort.setName("port" + index++);
            } else {
                containerPort.setName(port.getName());
            }

            containerPort.setContainerPort(port.getTargetPort() != null ? port.getTargetPort() : port.getPort());
            containerPort.setProtocol(port.getProtocol());

            result.add(containerPort);
        }

        return result;
    }

    /**
     * Searches the database configuration for a given name
     *
     * @param image The name of the database image
     * @return The corresponding database name or null otherwise
     */
    private DatabaseConfiguration getDatabaseConfiguration(String image) {
        for (DatabaseConfiguration db: databaseConfigurations.getDatabases()) {
            if (db.getName().equals(image)) {
                return db;
            }
        }

        return null;
    }

    /**
     * Serializes a Kubernetes Combined Object into a deployable Kubernetes YAML String
     *
     * @param obj The Kubernetes Combined object which should be serialized
     * @return The Kubernetes Combined object serialized as string in YAML format for Kubernetes
     * @throws IOException if their is a syntax error somewhere
     */
    public String serializeToYAML(KubernetesCombined obj) throws IOException {
        StringBuilder result = new StringBuilder();

        for (ConfigMap map: obj.getConfigMapList()) {
            result.append(yamlGenerator.transformObjectToYaml(map));
        }

        for (Secret secret: obj.getSecretList()) {
            result.append(yamlGenerator.transformObjectToYaml(secret));
        }

        if (obj.getDeployment() != null) {
            result.append(yamlGenerator.transformObjectToYaml(obj.getDeployment()));
        }

        if (obj.getStatefulSet() != null) {
            result.append(yamlGenerator.transformObjectToYaml(obj.getStatefulSet()));
        }

        for (de.th.bingen.master.backend.model.kubernetes.service.Service service: obj.getServiceList()) {
            result.append(yamlGenerator.transformObjectToYaml(service));
        }

        if (obj.getIngress() != null) {
            result.append(yamlGenerator.transformObjectToYaml(obj.getIngress()));
        }

        return result.toString();
    }
}
