import { BaseContainer } from '../BaseContainer'
import { EnvironmentVariable } from '../container/EnvironmentVariable';
import { RandomWord } from '../../services/randomWord';

/** Represents a Database request in the request */
export class Database extends BaseContainer {
    /** Name of the databse */
    name: string;

    /** Image of the database */
    image: string;

    /** Size of the database */
    size: number;

    /** Whether the database should be accessible externally */
    expose: boolean = false;

    /** With which service type the database should be exposed */
    type: string = "LoadBalancer";

    /** Environment Variables of the Databse */
    environments: EnvironmentVariable[] = [];

    /** Initializes the database with an unique identifier */
    constructor() {
        super();
        this.uniqueName = RandomWord.getRandomWordCombination();
    }
}