package de.th.bingen.master.backend.model.kubernetes.Ingress;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngressRule {
    private String host;
    private HttpIngressRuleValue http;

    public HttpIngressRuleValue getHttp() {
        return http;
    }

    public void setHttp(HttpIngressRuleValue http) {
        this.http = http;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
