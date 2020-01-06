import { Container } from "../container/Container";
import { Link } from "../Link";
import { Database } from "../database/Database";

/** Global Wizard options */
export class MainWizardOptions {
    /** The current page of the wizard */
    page: string = "START";

    /** The Kubernetes deployment YAML */
    deployment: string;

    /** Whether the wizard is used to deploy a single container or not */
    singleContainer: boolean;

    /** The current selected container */
    currentContainer: Container = null;

    /** Whether the user wants to edit the selected container */
    editContainer: boolean = false;

    /** Whether the user wants to add a new container */
    addContainer: boolean = false;

    /** The current selected link */
    currentLink: Link = null;

    /** Whether the user wants to add a link */
    addLink: boolean = false;

    /** The current selected database */
    currentDatabase: Database = null;

    /** Whether the user wants to edit a database */
    editDatabase: boolean = false;

    /** Wheter the user wants to add a database */
    addDatabase: boolean = false;
}