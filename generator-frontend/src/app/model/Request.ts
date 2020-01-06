import { Container } from './container/Container';
import { Service } from './service/Service';
import { Link } from './Link';
import { Database } from './database/Database';
import { dataType } from 'angular-http-deserializer/decorators';

/**
 * Request which will get sent to the backend
 */
export class Request {
    /** Name of the template */
    templateName: string;

    /** Array of requested containers */
    @dataType(Container, true)
    containers: Container[] = [];

    /** Array of requested databases */
    @dataType(Database, true)
    databases: Database[] = [];

    /** Array of requested services */
    @dataType(Service, true)
    services: Service[] = [];

    /** Array of requested links */
    @dataType(Link, true)
    links: Link[] = [];
}