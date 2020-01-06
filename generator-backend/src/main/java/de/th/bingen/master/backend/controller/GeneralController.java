package de.th.bingen.master.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.dockerjava.api.exception.NotFoundException;
import de.th.bingen.master.backend.configuration.DatabaseConfiguration;
import de.th.bingen.master.backend.configuration.DatabaseConfigurations;
import de.th.bingen.master.backend.model.request.KubernetesDeployRequest;
import de.th.bingen.master.backend.model.request.Request;
import de.th.bingen.master.backend.model.response.ImageAnalysis;
import de.th.bingen.master.backend.model.response.KubernetesCombined;
import de.th.bingen.master.backend.model.response.KubernetesDeployResponse;
import de.th.bingen.master.backend.model.response.TemplateResponse;
import de.th.bingen.master.backend.services.DockerService;
import de.th.bingen.master.backend.services.Generator;
import de.th.bingen.master.backend.services.KubernetesDeploymentService;
import de.th.bingen.master.backend.services.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.ProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class GeneralController {
    @Autowired
    private Generator generator;
    @Autowired
    private DatabaseConfigurations databaseConfigurations;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private DockerService dockerService;
    @Autowired
    private KubernetesDeploymentService kubernetesDeploymentService;

    @PostMapping(value = "/generate", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> generateMicroserviceArchitecture(@RequestBody @Valid @NotNull Request request) throws IOException {
        List<KubernetesCombined> resultFiles = generator.generateDistinctFiles(request);

        StringBuilder resultString = new StringBuilder();
        for (KubernetesCombined k: resultFiles) {
            resultString.append(generator.serializeToYAML(k));
        }

        return new ResponseEntity<>(resultString.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/getAvailableDatabases")
    public ResponseEntity<List<DatabaseConfiguration>> getAvailableDatabases() {
        return new ResponseEntity<>(databaseConfigurations.getDatabases(), HttpStatus.OK);
    }

    @GetMapping(path = "/templates")
    public ResponseEntity<List<String>> getTemplates() {
        List<String> names = new ArrayList<>();

        templateService.getTemplates().forEach(t -> names.add(t.getName()));
        Collections.sort(names);

        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    @GetMapping(path = "/templates/{template}")
    public ResponseEntity<TemplateResponse> getTemplate(@PathVariable(value = "template") String template) throws IOException {
        TemplateResponse response = templateService.getTemplate(template);

        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getImagesFromDockerHubApi(@RequestParam(value = "image") String image) {
        return new ResponseEntity<>(dockerService.searchImage(image), HttpStatus.OK);
    }

    @GetMapping(path = "/analyzeImage")
    public ResponseEntity<ImageAnalysis> getImageAnalysis(@RequestParam(value = "image") String image) throws InterruptedException, NotFoundException, ProcessingException, RuntimeException {
            return new ResponseEntity<>(dockerService.getImageAnalysis(image),HttpStatus.OK);
    }

    @GetMapping(path = "/image")
    public ResponseEntity<List<String>> getImagesFromDockerHub(@RequestParam(value = "image") String image) {
        ResponseEntity<String> response = new RestTemplate().getForEntity("https://registry.hub.docker.com/v1/search?q=" + image, String.class);
        ArrayList<String> result = new ArrayList<>();
        try {
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            ArrayNode results = (ArrayNode) root.get("results");

            for (int i = 0; i < results.size(); i++) {
                ObjectNode node = (ObjectNode) results.get(i);
                TextNode textNode = (TextNode) node.get("name");

                result.add(textNode.asText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/deploy")
    public ResponseEntity<KubernetesDeployResponse> deploy(@RequestBody @Valid @NotNull KubernetesDeployRequest request) {
        return new ResponseEntity<>(kubernetesDeploymentService.deploy(request), HttpStatus.OK);
    }

    @PostMapping(path = "/deleteDeployment")
    public ResponseEntity<KubernetesDeployResponse> deleteDeployment(@RequestBody @Valid @NotNull KubernetesDeployRequest request) {
        return new ResponseEntity<>(kubernetesDeploymentService.delete(request), HttpStatus.OK);
    }

    @PostMapping(path = "/getDeploymentStatus")
    public ResponseEntity<KubernetesDeployResponse> checkStatus(@RequestBody @Valid @NotNull KubernetesDeployRequest request) {
        return new ResponseEntity<>(kubernetesDeploymentService.getStatus(request), HttpStatus.OK);
    }
}
