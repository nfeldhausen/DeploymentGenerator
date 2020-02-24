/**
 * Container specifiy wizard options
 */
export class ContainerWizardOptions {
    /** Whether the container stores all its data in a database or not */
    databaseStore: boolean;

    /** Wheter the container stores persitent data */
    storesData: boolean;

    /** Whether the user authorizes scaling despite being a stateful container */
    authorizeScale: boolean;

    /** Whether the container supports to be configured with environment variables */
    envConfigurable: boolean;

    /** Whether the container support to be initialized with init containers */
    initConfigurable: boolean;

    /** Whether the user wants a health check for the container */
    probeConfigurable: boolean;

    /** Whether the container supports a HTTP Probe */
    httpConfigurable: boolean;
}