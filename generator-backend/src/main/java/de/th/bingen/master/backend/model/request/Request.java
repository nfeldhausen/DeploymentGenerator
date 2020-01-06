package de.th.bingen.master.backend.model.request;

import de.th.bingen.master.backend.model.request.container.Container;
import de.th.bingen.master.backend.model.request.service.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class Request {
    @Valid
    private List<Container> containers = new ArrayList<>();
    @Valid
    private List<Database> databases = new ArrayList<>();
    @Valid
    private List<Service> services = new ArrayList<>();
    @Valid
    private List<Link> links = new ArrayList<>();
    private String templateName;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public List<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(List<Database> databases) {
        this.databases = databases;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addContainer(Container container) {
        containers.add(container);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void addAllLinks(ArrayList<Link> links) {
        this.links.addAll(links);
    }
}
