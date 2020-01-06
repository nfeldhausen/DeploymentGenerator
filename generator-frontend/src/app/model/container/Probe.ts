/**
 * Liveness and Readiness Probe
 */
export class Probe {
    /** Amount of time the probe needs to fail until it's declared as unhealthy */
    failureThreshold: number = 1;

    /** Amount of time the probe waits before kubernetes starts checking the health of the application */
    initialDelaySeconds: number = 0;

    /** The amount of time between health checks */
    periodSeconds: number = 10;

    /** The amount of time a probes needs to succeed until the application is declared healthy */
    successThreshold: number = 1;

    /** The amount of time until the request of an application times out */
    timeoutSeconds: number = 1;

    /** The HTTP action the probe should execute */
    httpAction: string;

    /** The HTTP port which the HTTP action should address */
    httpPort: number = 80;

    /** The TCP port the porbe should address */
    tcpPort: number;

    constructor(private fThreshold?: number, private initDelay?: number, private periodSecond?: number,
        private succThreshold?: number, private timeout?: number) {
        this.failureThreshold = fThreshold || 1;
        this.initialDelaySeconds = initDelay || 0;
        this.periodSeconds = periodSecond || 10;
        this.successThreshold = succThreshold || 1;
        this.timeoutSeconds = timeout || 1;
    }

}