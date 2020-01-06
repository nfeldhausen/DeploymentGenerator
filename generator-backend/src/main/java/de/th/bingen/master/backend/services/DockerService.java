package de.th.bingen.master.backend.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;
import de.th.bingen.master.backend.model.request.Port;
import de.th.bingen.master.backend.model.request.container.EnvironmentVariable;
import de.th.bingen.master.backend.model.request.enums.ProtocolType;
import de.th.bingen.master.backend.model.response.ImageAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.ProcessingException;
import java.util.*;

@Service
public class DockerService {
    private DockerClient docker;
    private HashMap<String, ImageAnalysis> cachedImages = new HashMap<>();
    private HashSet<String> downloadList = new HashSet<>();

    /**
     * Creates a Docker Client
     *
     * @param dockerHost The Address of the Docker Engine API Host (either unix:// or tcp://)
     */
    public DockerService(@Value("${docker.host}") String dockerHost) {
        DefaultDockerClientConfig.Builder config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost(dockerHost).withDockerConfig(null).withRegistryUsername(null);

        docker = DockerClientBuilder.getInstance(config).build();
    }

    /**
     * Searches an image in the global dockerhub repository
     *
     * @param image The name of the image
     * @return A list of possible image names
     */
    public List<String> searchImage(String image) {
        List<SearchItem> result = docker.searchImagesCmd(image).exec();
        List<String> returnList = new ArrayList<>();

        for (SearchItem item : result) {
            returnList.add(item.getName());
        }

        return returnList;
    }

    /**
     * Returns the environment variables, ports and storage mount points of an image
     *
     * @param image The name of the image
     * @return An Image Analysis Object containing the environment variables, ports and storage mount points
     * @throws InterruptedException When the program get's terminated when it's currently downloading an image
     * @throws NotFoundException When the image does not exist (either local or in the remote registry)
     * @throws ProcessingException When the Docker Engine API Host is not available
     * @throws RuntimeException When the Docker Engine API Host gets disconnected while downloading an image
     */
    public ImageAnalysis getImageAnalysis(String image) throws InterruptedException, NotFoundException, ProcessingException, RuntimeException {
        if (!image.contains(":")) {
            image += ":latest";
        }

        String action = containsImageOrEnterDownloadMode(image);
        while (action.equals("WAIT")) {
            synchronized (this) {
                this.wait();
            }
            action = containsImageOrEnterDownloadMode(image);
        }

        if (action.equals("DOWNLOAD")) {
            downloadAndAnalyze(image);
        }

        return cachedImages.get(image);
    }

    /**
     * Downloads an image from a registry if it does not exist locally, analyzes the image and extracts environment variables, ports and storage mount points
     *
     * @param image The name of the docker image
     * @throws InterruptedException When the program get's terminated when it's currently downloading an image
     * @throws NotFoundException When the image does not exist (either local or in the remote registry)
     * @throws ProcessingException When the Docker Engine API Host is not available
     * @throws RuntimeException When the Docker Engine API Host gets disconnected while downloading an image
     */
    public void downloadAndAnalyze(String image) throws InterruptedException, NotFoundException, ProcessingException, RuntimeException {
        boolean delete = false;

        try {
            if (!registryContainsImage(image)) {
                downloadImage(image);
                delete = true;
            }

            InspectImageResponse inspectImage = docker.inspectImageCmd(image).exec();
            ContainerConfig config = inspectImage.getConfig();

            ImageAnalysis analysis = new ImageAnalysis();
            analysis.setName(image);

            if (config.getEnv() != null) {
                List<EnvironmentVariable> envs = new ArrayList<>();

                for (String env : config.getEnv()) {
                    EnvironmentVariable variable = new EnvironmentVariable();

                    String[] envParts = env.split("=");
                    variable.setName(envParts[0]);
                    if (envParts.length > 1) {
                        variable.setValue(envParts[1]);
                    }

                    envs.add(variable);
                }

                analysis.setEnvironments(envs);
            }

            if (config.getExposedPorts() != null) {
                List<Port> ports = new ArrayList<>();

                for (ExposedPort port : config.getExposedPorts()) {
                    Port requestPort = new Port();
                    requestPort.setPort(port.getPort());
                    requestPort.setProtocol(ProtocolType.valueOf(port.getProtocol().toString().toUpperCase()));

                    ports.add(requestPort);
                }

                analysis.setPorts(ports);
            }

            if (config.getVolumes() != null) {
                List<String> volumes = new ArrayList<>();

                for (Map.Entry<String, ?> entry: config.getVolumes().entrySet()) {
                    volumes.add(entry.getKey());
                }

                analysis.setMountPaths(volumes);
            }

            if (delete) {
                docker.removeImageCmd(image).exec();
            }

            exitDownloadMode(analysis);
        } catch (InterruptedException ex) {
            abortDownload(image);
            throw new InterruptedException(ex.getMessage());
        } catch (NotFoundException ex) {
            abortDownload(image);
            throw new NotFoundException(ex.getMessage());
        } catch (DockerClientException ex) {
            abortDownload(image);
            throw new DockerClientException(ex.getMessage());
        } catch (InternalServerErrorException ex) {
            abortDownload(image);
            throw new InternalServerErrorException(ex.getMessage());
        } catch (ProcessingException ex) {
            abortDownload(image);
            throw new ProcessingException(ex.getMessage());
        } catch (RuntimeException ex) {
            abortDownload(image);
            throw new RuntimeException(ex.getMessage());
        } catch (Exception ex) {
            abortDownload(image);
            System.err.println("Unknown Exception occured! " + ex.getClass());
        }
    }

    /**
     * Aborts the download of an image and notifies all waiting threads
     *
     * @param image The name of the image
     */
    private synchronized void abortDownload(String image) {
        downloadList.remove(image);
        this.notifyAll();
    }

    /**
     * Checks whether the image already got analyzed, if it is being downloaded or if it has to be downloaded and analyzed
     *
     * @param image The name of the docker image
     * @return A String telling the thread what to do (Possible values: RETURN, WAIT, DOWNLOAD)
     */
    private synchronized String containsImageOrEnterDownloadMode(String image) {
        if (cachedImages.containsKey(image)) {
            return "RETURN";
        }

        if (downloadList.contains(image)) {
            return "WAIT";
        }

        downloadList.add(image);
        return "DOWNLOAD";
    }

    /**
     * Exits the download mode of an image normally, provides the analyzed image for other threads and notifies all waiting threads
     *
     * @param image The name of the docker image
     */
    private synchronized void exitDownloadMode(ImageAnalysis image) {
        cachedImages.put(image.getName(), image);
        downloadList.remove(image.getName());

        this.notifyAll();
    }

    /**
     * Downloads an image from a docker registry
     *
     * @param image The name of the docker image
     * @throws InterruptedException When the program get's terminated when it's currently downloading an image
     * @throws NotFoundException When the image does not exist (either local or in the remote registry)
     */
    private void downloadImage(String image) throws InterruptedException, NotFoundException {
        docker.pullImageCmd(image).exec(new PullImageResultCallback()).awaitCompletion();
    }

    /**
     * Checks whether the registry of the Docker Engine API host contains the requested image or not
     *
     * @param imageName The name of the docker image
     * @return Whether the local Docker Engine API Host contains the image or not
     */
    private boolean registryContainsImage(String imageName) {
        List<Image> images = docker.listImagesCmd().exec();

        for (Image image : images) {
            if (image.getRepoTags() != null) {
                for (String tag : image.getRepoTags()) {
                    if (tag.equals(imageName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
