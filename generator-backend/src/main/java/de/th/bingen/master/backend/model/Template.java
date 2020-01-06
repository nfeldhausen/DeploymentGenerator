package de.th.bingen.master.backend.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.th.bingen.master.backend.model.response.KubernetesCombined;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Template {
    private String name;
    private List<KubernetesCombined> combinedList;
    private HashMap<String, Object> description;

    public Template() {
    }

    public Template(List<KubernetesCombined> combinedList) {
        this.combinedList = combinedList;
    }

    public HashMap<String, Object> getDescription() {
        return description;
    }

    public void setDescription(HashMap<String, Object> description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<KubernetesCombined> getCombinedList() {
        return combinedList;
    }

    public void setCombinedList(List<KubernetesCombined> combinedList) {
        this.combinedList = combinedList;
    }

    public void addKubernetesCombined(KubernetesCombined combined) {
        if (combinedList == null) {
            combinedList = new ArrayList<>();
        }

        combinedList.add(combined);
    }

    /**
     * Deep copies a template
     *
     * @return A deep copy of the template
     * @throws IOException When the template could not be copied
     */
    public Template deepCopy() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this);
        return mapper.readValue(json, Template.class);
    }
}
