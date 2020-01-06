package de.th.bingen.master.backend.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import de.th.bingen.master.backend.model.Template;
import de.th.bingen.master.backend.model.kubernetes.Ingress.Ingress;
import de.th.bingen.master.backend.model.kubernetes.configMap.ConfigMap;
import de.th.bingen.master.backend.model.kubernetes.deployment.Deployment;
import de.th.bingen.master.backend.model.kubernetes.secret.Secret;
import de.th.bingen.master.backend.model.kubernetes.statefulset.StatefulSet;
import de.th.bingen.master.backend.model.response.KubernetesCombined;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import javax.validation.constraints.Null;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Service
public class TemplateLoader {
    List<Template> templates;

    public TemplateLoader() {
        loadTemplates();
    }

    public List<Template> getTemplates() {
        return templates;
    }

    /**
     * Loads all Template files from the folder kubernetesTemplates.
     */
    public void loadTemplates() {
        templates = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            for (String templateFolderName : getFileListing("/kubernetesTemplates")) {
                Template template = new Template();
                template.setName(templateFolderName);
                int templateIndex = 0;

                try {
                    for (String templateFileName : getFileListing("/kubernetesTemplates/" + templateFolderName)) {
                        KubernetesCombined combined = new KubernetesCombined();
                        combined.setTemplateName("template" + templateIndex++);

                        if (!templateFileName.equals("description.yaml")) {

                            for (Object obj : new Yaml().loadAll(getClass().getResourceAsStream("/kubernetesTemplates/" + templateFolderName + "/" + templateFileName))) {
                                HashMap<String, String> objHashMap = (HashMap<String, String>) obj;
                                String objString = mapper.writeValueAsString(objHashMap);

                                switch (objHashMap.get("kind")) {
                                    case "Deployment":
                                        combined.setDeployment(mapper.readValue(objString, Deployment.class));
                                        break;
                                    case "Service":
                                        combined.addService(mapper.readValue(objString, de.th.bingen.master.backend.model.kubernetes.service.Service.class));
                                        break;
                                    case "Secret":
                                        combined.addSecret(mapper.readValue(objString, Secret.class));
                                        break;
                                    case "ConfigMap":
                                        combined.addConfigMap(mapper.readValue(objString, ConfigMap.class));
                                        break;
                                    case "StatefulSet":
                                        combined.setStatefulSet(mapper.readValue(objString, StatefulSet.class));
                                        break;
                                }
                            }

                            template.addKubernetesCombined(combined);
                        } else {
                            template.setDescription(new Yaml().load(getClass().getResourceAsStream("/kubernetesTemplates/" + templateFolderName + "/" + templateFileName)));
                        }
                    }

                    templates.add(template);
                } catch (UnrecognizedPropertyException e) {
                    String message = e.getMessage().substring(e.getMessage().indexOf("\"") + 1);
                    String propertyName = message.substring(0, message.indexOf("\""));

                    message = message.substring(message.indexOf("(") + 1);

                    String className = message.substring(0, message.indexOf(")"));
                    className = className.substring(Math.max(0, className.lastIndexOf(".") + 1));

                    System.err.println("Could not add template " + templateFolderName + " because property " + propertyName + " is unknown in Kubernetes Class " + className);
                } catch (URISyntaxException | NullPointerException | IOException e) {
                    System.err.println("Template Folder " + templateFolderName + " is not a folder or does not contain any files");
                }

            }

        } catch (URISyntaxException | NullPointerException | IOException e) {
            System.err.println("kubernetesTemplates folder does not exist or does not contain any files.");
        }

    }

    /**
     * Lists all files of the kubernetesTemplates folder in the resource folder, depending on where it is located.
     * It is necessary to use another method to list all files in a jar than on a normal file system.
     *
     * @param path The relative path in the resource folder starting with a /
     * @return The List of folders (or files which are sadly not distinguishable from folders)
     * @throws URISyntaxException When the Path can not be transformed to URI
     * @throws IOException When the Path could not be transformed to a string
     * @throws NullPointerException When the folder does not exist or is empty
     */
    private HashSet<String> getFileListing(final String path) throws URISyntaxException,
            IOException, NullPointerException {
        HashSet<String> result = new HashSet<>();
        URL dirURL = TemplateLoader.class.getResource(path);

        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            result.addAll(Arrays.asList(new File(dirURL.toURI()).list()));
            return result;
        }

        if (dirURL == null) {
            // In case of a jar file, we can't actually find a directory.
            // Have to assume the same jar as clazz.
            final String me = TemplateLoader.class.getName().replace(".", "/") + ".class";
            dirURL = TemplateLoader.class.getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) { /* A JAR path */
            // strip out only the JAR file
            final String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            final JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                final int pathIndex = name.lastIndexOf(path);
                if (pathIndex > 0) {
                    String nameWithPath = name.substring(name.lastIndexOf(path));
                    nameWithPath = nameWithPath.substring(path.length());

                    if (nameWithPath.indexOf("/") >= 0) {
                        nameWithPath = nameWithPath.substring(nameWithPath.indexOf("/") + 1);
                    }

                    if (nameWithPath.indexOf("/") >= 0) {
                        nameWithPath = nameWithPath.substring(0, nameWithPath.indexOf("/"));
                    }

                    if (!nameWithPath.trim().equals("")) {
                        result.add(nameWithPath);
                    }
                }
            }

            jar.close();
            return result;
        }
        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }
}
