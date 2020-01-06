
/**
 * Class for Deploying the YAML File directly to Kubernetes
 */
export class KubernetesDeployRequest {
    /** Address of the Kubernetes Master */
    host: string;

    /** Kubernetes Cluster Admin Token */
    token: string;

    /** Deployment File which should be deployed in Kubernetes */
    deployment: string;

    /**
     * Initializes a new request
     * 
     * @param h Kubernetes Master Address
     * @param t Kubernetes Cluster Admin Token
     * @param d Deployment File
     */
    constructor(h: string, t: string, d: string) {
        this.host = h;
        this.token = t;
        this.deployment = d;
    }
}