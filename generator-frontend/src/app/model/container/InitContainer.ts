import { BaseContainer } from "../BaseContainer";
import { RandomWord } from "../../services/randomWord";

/**
 * InitContainer for initializing containers
 */
export class InitContainer extends BaseContainer {
    /** Name of the InitContainer */
    name: string;

    /** The command which the container should execute */
    command: string;

    /** The mountPath of the init container for storing data for the container */
    mountPath: string;

    /** Whether the container is privileged and can execute commands which need root rigths*/
    privileged: boolean;
    
    /** Image for the init container */
    image: string = "ubuntu:bionic"
    
    /**
     * Initializes the container with an unique identifier
     */
    constructor() {
        super();
        this.uniqueName = "init-" + RandomWord.getRandomWordCombination();
    }
}