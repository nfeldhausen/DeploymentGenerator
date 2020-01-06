import { Port } from "../Port";
import { BaseContainer } from "../BaseContainer";
import { v4 as uuid} from 'uuid';
import { dataType } from "angular-http-deserializer/decorators";

/**
 * TCPUDPOptions for Ingress Services
 */
export class TCPUDPOption extends BaseContainer {
    /** Unique name of a container */
    containerName: string;

    /** Port of the container */
    @dataType(Port)
    port: Port;

    /** Initializes the option with an unique identifier */
    constructor() {
        super();
        this.uniqueName = "TU-" + uuid();
    }
}