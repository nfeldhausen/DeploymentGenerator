import { BaseContainer } from "../BaseContainer";
import { v4 as uuid} from 'uuid';

/**
 * Environment Variables for containers
 */
export class EnvironmentVariable extends BaseContainer {
    /** Name of the environment variables */
    name: string;

    /** Value of the environment variables */
    value: string;

    /** Tells the backend whether the environment variable should be in a Config Map or in a Secret */
    secret: boolean = false;

    /**
     * Initializes the variable with an unique identifier
     */
    constructor() {
        super();
        this.uniqueName = "v-" + uuid();
    }
}