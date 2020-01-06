package de.th.bingen.master.backend.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.th.bingen.master.backend.model.request.Request;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateResponse {
    private String result;
    private Request request = new Request();
    private HashMap<String, Object> description;

    public HashMap<String, Object> getDescription() {
        return description;
    }

    public void setDescription(HashMap<String, Object> description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
