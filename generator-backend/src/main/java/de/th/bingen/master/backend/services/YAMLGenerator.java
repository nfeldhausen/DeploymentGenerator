package de.th.bingen.master.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class YAMLGenerator {
    /**
     * Serializes a given Object to a json string
     *
     * @param obj The object which should be serialized
     * @return The object serialized as a json string
     * @throws JsonProcessingException if something goes wrong
     */
    public String serializeToString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Transforms a json string into a YAML string
     *
     * @param json The json string which should be transformed into a YAML string
     * @return The string in YAML format
     * @throws IOException if something goes wrong
     */
    public String generateYAML(String json) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        Object obj = jsonMapper.readValue(json, Object.class);

        ObjectMapper yamlWrite = new ObjectMapper(new YAMLFactory());
        return yamlWrite.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Serializes a given object to a YAML string
     *
     * @param obj The object which should be serialized into a YAML string
     * @return The object serialized as a YAML String
     * @throws IOException if something goes wrong
     */
    public String transformObjectToYaml(Object obj) throws IOException {
        return generateYAML(serializeToString(obj));
    }
}
