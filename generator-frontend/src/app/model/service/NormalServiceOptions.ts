import { BaseContainer } from "../BaseContainer";
import { Port } from "../Port";
import { v4 as uuid} from 'uuid';
import { dataType } from "angular-http-deserializer/decorators";

/**
 * Options for NodePort and LoadBalancer services
 */
export class NormalServiceOptions extends BaseContainer {
    /** Unique Name of the container */
    containerName: String;

    /** Ports which should be exposed with this service */
    @dataType(Port, true)
    ports: Port[] = [];

    /** Whether to use stick sessions or not */
    sessionAffinity: boolean;

    /**
     * Initializes the normal service options with an unique identifier
     */
    constructor() {
        super();
        this.uniqueName = 'nso-' + uuid();
    }

    /**
     * Adds a port to the service
     */
    addPort(): void {
        this.ports.push(new Port());
    }

    /**
     * Deletes a port from the service
     * 
     * @param port The port which should get deleted
     */
    deletePort(port: Port) {
        this.ports = this.ports.filter(p => p !== port);
    }
}