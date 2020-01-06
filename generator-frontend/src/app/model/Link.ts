import { BaseContainer } from "./BaseContainer";
import { v4 as uuid} from 'uuid';

/** Represents a link in the request */
export class Link extends BaseContainer {
    /** Name of the environment variable */
    name: string;

    /** Unique name of the container where the environment variable should be created */
    containerFrom: string;

    /** Unique name of the container (Value of the environment variable) */
    containerTo: string;

    /** Scheme of the link (should contain <link>) */
    scheme: string;

    /**
     * Initializes the Link with an unique name
     */
    constructor() {
        super();
        this.uniqueName = "l-" + uuid();
    }
}