/**
 * Kubernetes resource requests and limits
 */
export class KubernetesResource {
    /** The amount of cpu in milli cpu */
    cpu: number;

    /** The amount of memory in MB */
    memory: number;

    /**
     * Initializes the resource with given numbers
     * 
     * @param cp The cpu request in milli
     * @param mem The memory request in MB
     */
    constructor(cp?: number, mem?: number) {
        this.cpu = cp;
        this.memory = mem;
    }
}