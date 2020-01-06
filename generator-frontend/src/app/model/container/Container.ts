import { EnvironmentVariable } from './EnvironmentVariable';
import { Port } from '../Port';
import { v4 as uuid} from 'uuid';
import { BaseContainer } from '../BaseContainer';
import { RandomWord } from '../../services/randomWord';
import { Storage } from './Storage';
import { ContainerAdvancedOptions } from './ContainerAdvancedOptions';
import { dataType } from 'angular-http-deserializer/decorators';
import { ContainerWizardOptions } from '../wizard/ContainerWizardOptions';
import { ImageAnalysis } from '../ImageAnalysis';

/**
 * Represents a Container request in Kubernetes
 */
export class Container extends BaseContainer {
    /** Name of the template, if received a template */
    templateName: string;

    /** Name of the Containr */
    name: string;

    /** Image of the Container */
    image: string;

    /** Desired amount of Kubernetes pod instances */
    replicas: number = 1;

    /** Array containing all environment variables of the container */
    @dataType(EnvironmentVariable, true)
    environments: EnvironmentVariable[] = [];

    /** Array containing all the ports of the container */
    @dataType(Port, true)
    ports: Port[] = [];

    /** Boolean if all ports of the container should get exposed */
    expose: boolean = false;

    /** With which Service Type the Ports should be exposed */
    exposeType: string = "NodePort";

    /** Array containing all storage of the container */
    @dataType(Storage, true)
    storage: Storage[] = [];

    /** Defines if advanced mod */
    advancedOptions: boolean = false;

    /** Container Advanced Options like requests etc */
    @dataType(ContainerAdvancedOptions)
    containerAdvancedOptions: ContainerAdvancedOptions = new ContainerAdvancedOptions();

    /** Options for the wizard (most likely the questions) */
    wizardOptions: ContainerWizardOptions;

    /** Wheter angular is loading an analysis for the container or not */
    loadingAnalysis: boolean = false;

    /** The analysis for the image */
    analysis: ImageAnalysis = null;

    /** Sets an unique name for the container */
    constructor() {
        super();
        this.uniqueName = RandomWord.getRandomWordCombination();
    }

    /** Adds an environment variable to the container */
    addEnvironmentVariable(): void {
        this.environments.push(new EnvironmentVariable());
    }

    /**
     * Deletes an environment variable from the container
     * 
     * @param variable The environment variable which should get deleted from the container
     */
    deleteEnvironmentVariable(variable: EnvironmentVariable): void {
        this.environments = this.environments.filter(v => v !== variable);
    }

    /** Adds a port to the container */
    addPort(): void {
        this.ports.push(new Port());
    }

    /**
     * Deletes a port from the container
     * 
     * @param port The port which should get deleted
     */
    deletePort(port: Port): void {
        this.ports = this.ports.filter(p => p !== port);
    }

    /** Adds storage to the container */
    addStorage(): void {
        this.storage.push(new Storage());
    }

    /** Deletes storage from the container
     * 
     * @param storage The storage which schould get deleted from the container
     */
    deleteStorage(storage: Storage): void {
        this.storage = this.storage.filter(s => s !== storage);
    }
}