import { Port } from "../Port";
import { BaseContainer } from "../BaseContainer";
import { v4 as uuid } from 'uuid';
import { IngressOptions } from "./IngressOptions";
import { NormalServiceOptions } from "./NormalServiceOptions";
import { RandomWord } from "../../services/randomWord";
import { dataType } from "angular-http-deserializer/decorators";

/** Represents a service in the request */
export class Service extends BaseContainer {
    /** Name of the service */
    name: string;

    /** Service Type of the Service */
    type: string = "NodePort";

    /** Last service type (for displaying reasons) */
    lastType: string = this.type;

    /** NormalServiceOptions for a service (Either for NodePort or LoadBalancer service) */
    @dataType(NormalServiceOptions)
    normalServiceOptions: NormalServiceOptions;

    /** IngressOptions for Ingress services */
    @dataType(IngressOptions)
    ingressOptions: IngressOptions;

    /**
     * Initializes a new service with an unique identifier and NormalSerivceOptions as standard
     */
    constructor() {
        super();
        this.uniqueName = RandomWord.getRandomWordCombination();
        this.normalServiceOptions = new NormalServiceOptions();
    }

    /**
     * Handles if service type changes from NodePort or LoadBalancer to Ingress
     */
    typeChanged() {
        if (this.type === 'Ingress') {
            this.normalServiceOptions = null;
            this.ingressOptions = new IngressOptions();
            this.lastType = this.type;
        } else {
            if (this.lastType !== 'NodePort' && this.lastType !== 'LoadBalancer') {
                this.normalServiceOptions = new NormalServiceOptions();
                this.ingressOptions = null;
                this.lastType = this.type;
            }
        }
    }
}