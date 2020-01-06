package de.th.bingen.master.backend.services;

import de.th.bingen.master.backend.model.request.KubernetesDeployRequest;
import de.th.bingen.master.backend.model.response.KubernetesDeployResponse;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.LoadBalancerIngress;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.client.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class KubernetesDeploymentService {

    public KubernetesDeploymentService() { }

    /**
     * Deploys a Deployment in Kubernetes
     *
     * @param request The request consisting of Kubernetes Master address, Cluster Admin Token and Deployment File
     * @return A KubernetesDeployResponse with the status whether the deployment was successful or not
     */
    public KubernetesDeployResponse deploy(KubernetesDeployRequest request) {
        KubernetesClient client = getClient(request.getHost(), request.getToken());

        InputStream is = new ByteArrayInputStream(request.getDeployment().getBytes());

        try {
            client.load(is).inNamespace("default").createOrReplace();
            return new KubernetesDeployResponse("Success","Deployment created or updated!");
        } catch (IllegalArgumentException ex) {
            return new KubernetesDeployResponse("Error","Error in YAML file");
        } catch (KubernetesClientException ex) {
            if (ex.getMessage().contains("Unauthorized")) {
                return new KubernetesDeployResponse("Error", "Unauthorized!");
            } else {
                if (ex.getMessage().contains("server could not find the requested resource")) {
                    return new KubernetesDeployResponse("Error","Error in YAML file");
                } else {
                    if (ex.getMessage().contains("Failure executing: PUT")) {
                        return new KubernetesDeployResponse("Warning", "Some of the given resources could not be updated, because they are immutable.");
                    } else {
                        return new KubernetesDeployResponse("Error", "Could not connect to the Kubernetes master server");
                    }
                }
            }
        }
    }

    /**
     * Deletes a Deployment File from Kubernetes
     *
     * @param request The request consisting of Kubernetes Master address, Cluster Admin Token and Deployment File
     * @return A KubernetesDeployResponse with the status whether the deployment was successfully deleted or not
     */
    public KubernetesDeployResponse delete(KubernetesDeployRequest request) {
        KubernetesClient client = getClient(request.getHost(), request.getToken());

        InputStream is = new ByteArrayInputStream(request.getDeployment().getBytes());

        try {
            Boolean deleted = client.load(is).inNamespace("default").delete();

            if (deleted) {
                return new KubernetesDeployResponse("Success","Deleted deployment!");
            } else {
                return new KubernetesDeployResponse("Warning","Could not delete all resources or some resources may not exist!");
            }
        } catch (KubernetesClientException ex) {
            if (ex.getCause() != null) {
                return new KubernetesDeployResponse("Error", "Could not connect to the Kubernetes master server");
            } else {
                return new KubernetesDeployResponse("Error", "Unauthorized!");
            }
        }
    }

    /**
     * Fetches all Service Endpoints of externally accessible services
     *
     * @param request The request consisting of Kubernetes Master address, Cluster Admin Token and Deployment File
     * @return A KubernetesDeployResponse with a map containing all service endpoints
     */
    public KubernetesDeployResponse getStatus(KubernetesDeployRequest request) {
        KubernetesClient client = getClient(request.getHost(), request.getToken());

        List<Node> nodes;
        try {
            nodes = client.nodes().list().getItems();
        } catch (Exception ex) {
            if (ex.getCause() != null) {
                return new KubernetesDeployResponse("Error", "Could not connect to the Kubernetes master server");
            } else {
                return new KubernetesDeployResponse("Error", "Unauthorized!");
            }
        }

        InputStream is = new ByteArrayInputStream(request.getDeployment().getBytes());
        List <HasMetadata> metadata;
        try {
            metadata = client.load(is).get();
        } catch (Exception ex) {
            return new KubernetesDeployResponse("Error", "Error in YAML file");
        }

        HashMap<String, List<String>> serviceEndpoints = new HashMap<>();

        try {
            for (HasMetadata yaml : metadata) {
                if (yaml.getKind().equals("Service")) {
                    io.fabric8.kubernetes.api.model.Service service = client.services().inNamespace("default").withName(yaml.getMetadata().getName()).get();

                    if (service != null && service.getSpec().getType().equals("NodePort")) {
                        List<String> endPoints = new ArrayList<>();

                        String address = nodes.get(0).getStatus().getAddresses().get(0).getAddress();

                        for (ServicePort port : service.getSpec().getPorts()) {
                            endPoints.add(address + ":" + port.getNodePort());
                        }

                        serviceEndpoints.put(service.getMetadata().getName(), endPoints);
                    }

                    if (service != null && service.getSpec().getType().equals("LoadBalancer")) {
                        List<String> endPoints = new ArrayList<>();

                        for (LoadBalancerIngress ingress : service.getStatus().getLoadBalancer().getIngress()) {
                            String ip = ingress.getIp() + ":";

                            for (ServicePort port : service.getSpec().getPorts()) {
                                endPoints.add(ip + port.getPort());
                            }
                        }

                        if (endPoints.isEmpty()) {
                            endPoints.add("Service not available yet!");
                        }

                        serviceEndpoints.put(service.getMetadata().getName(), endPoints);
                    }
                }
            }
        } catch (Exception ex) {
            return new KubernetesDeployResponse("Error","Lost connection to the Kubernetes Master");
        }

        KubernetesDeployResponse response = new KubernetesDeployResponse("Success","Successfully fetched all service endpoints");
        response.setServiceEndpoints(serviceEndpoints);

        return response;
    }

    /**
     * Creates a new client for Kubernetes
     *
     * @param host A Kubernetes Master address
     * @param token A Kubernetes Cluster Admin Token
     * @return A Kubernetesclient with the provided address and token
     */
    private KubernetesClient getClient(String host, String token) {
        Config config = new ConfigBuilder().withCaCertFile(null).withClientCertFile(null).withClientKeyFile(null).withTrustCerts(true)
                .withMasterUrl(host).withOauthToken(token).build();

        return new DefaultKubernetesClient(config);
    }
}
