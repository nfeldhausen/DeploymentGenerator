/**
 * Response of a Deploy Request 
 */
export class KubernetesDeployResponse {
    /** Status of the Deployment Process: Success, Warning or Error */
    status: string;

    /** Message of the Deployment Process */
    message: string;

    /** Map with all Endpoints of a NodePort or LoadBalancer service */
    serviceEndpoints: Map<string, string[]>;
}