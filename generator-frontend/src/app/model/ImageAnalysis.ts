import { EnvironmentVariable } from "./container/EnvironmentVariable";
import { Port } from "./Port";

/**
 * Image analysis which contains information about the image extracted from the Dockerfile
 */
export class ImageAnalysis {
    /** Name of the image */
    name: string;

    /** Environment variables which are exposed in the Dockerfile */
    environments: EnvironmentVariable[];

    /** Ports which are exposed in the Dockerfile */
    ports: Port[];

    /** MountPaths which are exposed in the Dockerfile */
    mountPaths: string[];
}