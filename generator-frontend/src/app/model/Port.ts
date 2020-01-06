import { BaseContainer } from "./BaseContainer";
import { v4 as uuid} from 'uuid';

/**
 * Ports for Containers and Services
 */
export class Port extends BaseContainer {
    /** Name of the port */
    name: string;

    /** Port */
    port: number;

    /** TargetPort in Kubernetes */
    targetPort: number;

    /** Protocol of the Port */
    protocol: string = "TCP";

    /** Initializes the port with an unique identifier */
    constructor() {
        super();
        this.uniqueName = "p-" + uuid();
    }
}
