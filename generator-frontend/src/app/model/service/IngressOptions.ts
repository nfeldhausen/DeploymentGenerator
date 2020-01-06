import { BaseContainer } from "../BaseContainer";
import { v4 as uuid} from 'uuid';
import { TCPUDPOption } from "./TCPUDPOption";
import { HttpOption } from "./HttpOption";
import { dataType } from "angular-http-deserializer/decorators";

/** Options for an ingress service */
export class IngressOptions extends BaseContainer {
    /** With which service type the ingress controller should be exposed */
    serviceType: string = "NodePort";

    /** Sets sticky sessions */
    affinity: boolean = false;

    /** TCP/UDP Options */
    @dataType(TCPUDPOption, true)
    tcpUdpOptions: TCPUDPOption[] = [];

    /** HTTP Options */
    @dataType(HttpOption, true)
    httpOptions: HttpOption[] = [];

    /**
     * Initializes the ingress options with an unique identifier
     */
    constructor() {
        super();
        this.uniqueName = "i-" + uuid();
    }

    /**
     * Adds an TCPUDPOption
     */
    addIngressContainer() {
        this.tcpUdpOptions.push(new TCPUDPOption());
    }

    /**
     * Deletes and TCPUDPOption
     * 
     * @param ingContainer The TCPUDPOption which should get deleted
     */
    deleteIngressContainer(ingContainer: TCPUDPOption) {
        this.tcpUdpOptions = this.tcpUdpOptions.filter(c => c !== ingContainer);
    }

    /**
     * Adds an empty HTTP Option to the service
     */
    addHttpOption() {
        this.httpOptions.push(new HttpOption());
    }

    /**
     * Deletes a HTTP Option from the service
     * 
     * @param option The HTTP option which should be deleted
     */
    deleteHttpOption(option: HttpOption) {
        this.httpOptions = this.httpOptions.filter(c => c !== option);
    }
}