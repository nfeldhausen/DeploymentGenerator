import { EnvironmentVariable } from "../container/EnvironmentVariable";

/** DatabaseConfiguration which comes from the backend */
export class DatabaseConfiguration {
    /** Name of the database */
    name: string;

    /** Environment variables of the database */
    environmentVariables: EnvironmentVariable[];
}