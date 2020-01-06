import { BaseContainer } from "../BaseContainer";
import { v4 as uuid } from 'uuid';
import { HttpPath } from "./HttpPath";
import { dataType } from "angular-http-deserializer/decorators";

/** HTTP Options for Ingress Service */
export class HttpOption extends BaseContainer {
    /** Hostname of the service */
    host: string;

    /** Array containing all paths under this address */
    @dataType(HttpPath, true)
    paths: HttpPath[] = [];

    /**
     * Initializes the HttpOption with an unique identifier and an empty Http Path
     */
    constructor() {
        super();

        this.uniqueName = "ho-" + uuid();
        this.paths.push(new HttpPath());
    }

    /**
     * Adds an empty Http path to the service
     */
    addPath() {
        this.paths.push(new HttpPath());
    }

    /**
     * Deletes an Http path from the service
     * 
     * @param path The path which should get deleted from the request
     */
    deletePath(path: HttpPath) {
        this.paths = this.paths.filter(c => c !== path);
    }

    /**
     * Adds or deletes a Http Path from the service
     * 
     * @param event The HTMLButton
     * @param path The html path, if it needs to be deleted
     */
    pathManipulation(event: Event, path: HttpPath) {
        let element = <HTMLButtonElement>event.currentTarget;

        if (element.classList.contains('btn-primary')) {
            element.classList.remove('btn-primary');
            element.classList.add('btn-danger');
            element.innerHTML = '<i class="fas fa-minus" title="Delete Rule">';

            this.addPath();
        } else {
            element.classList.add('btn-primary');
            element.classList.remove('btn-danger');
            element.innerHTML = '<i class="fas fa-plus" title="Add Rule">';

            this.deletePath(path);
        }
    }
}