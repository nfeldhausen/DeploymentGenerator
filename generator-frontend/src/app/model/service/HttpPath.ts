import { BaseContainer } from "../BaseContainer";
import { v4 as uuid} from 'uuid';

/**
 * Http path for Http options
 */
export class HttpPath extends BaseContainer {
    /** Unique name of the container */
    container: string;

    /** Optional Subpath of the path */
    path: string;

    /** Port of the providing backend */
    port: number;

    /**
     * Initializes the Http Path with an unique identifier
     */
    constructor() {
        super();
        this.uniqueName = "hp-" + uuid();
    }

}