import { BaseContainer } from '../BaseContainer';
import { Probe } from './Probe';
import { KubernetesResource } from './KubernetesResource';
import { dataType } from 'angular-http-deserializer/decorators';
import { InitContainer } from './InitContainer';
/**
 * Container Advanced Options like TerminationGracePeriod or limits etc.
 */
export class ContainerAdvancedOptions extends BaseContainer {
    /** The amount of time Kubernetes waits before killing the container */
    gracePeriod: number = 15;

    /** Whether the container exposes a http service or not */
    httpService: boolean = false;

    /** Whether the container should be privileged or not */
    privileged: boolean;

    /** The readiness probe of the container */
    @dataType(Probe)
    readinessProbe: Probe;

    /** The liveness probe of the container */
    @dataType(Probe)
    livenessProbe: Probe;

    /** Resource Requests for Kubernetes */
    @dataType(KubernetesResource)
    requests: KubernetesResource = new KubernetesResource();

    /** Resource Limits for Kubernetes */
    @dataType(KubernetesResource)
    limits: KubernetesResource = new KubernetesResource();

    /** InitContainers for the Container */
    @dataType(InitContainer,true)
    initContainers: InitContainer[] = [];

    /**
     * Initializes the advanced options with a readiness probe
     */
    constructor() {
        super();
        this.readinessProbe = new Probe(3,10,10,1,1);
    }

    /**
     * Adds an init container to the container
     */
    addInitContainer() {
        this.initContainers.push(new InitContainer())
    }

    /**
     * Deletes an init container from the container
     * 
     * @param containter The init container which should get deleted from the container
     */
    deleteInitContainer(containter: InitContainer) {
        this.initContainers = this.initContainers.filter(c => c !== containter);
    }

    /**
     * Sets standard values for requests and limits
     */
    setStandardValues() {
        this.requests = new KubernetesResource(0.1, 128);
        this.limits = new KubernetesResource(0.2, 256);
    }

    /**
     * Either returns the existing readiness Probe or creates a new one
     * 
     * @returns The readiness probe of the container
     */
    getReadinessProbe(): Probe {
        if (this.readinessProbe === null || this.readinessProbe === undefined) {
            this.readinessProbe = new Probe(3,10,10,1,1);
        }

        return this.readinessProbe;
    }

    /**
     * Adds a liveness probe or deletes it
     */
    switchLivenessProbe(): void {
        if (this.livenessProbe === null || this.livenessProbe === undefined) {
            this.livenessProbe = new Probe(3,10,10,1,1);
        } else {
            this.livenessProbe = null;
        }
    }
}