import { Request } from "./Request";
import { dataType, skip } from "angular-http-deserializer/decorators";

/**
 * Template Response containing a deployment file, a request for later modifications and a optional description
 */
export class TemplateResponse {
    /** The Kubernetes YAML Deployment fiel */
    result: string;

    /** The Request for later modifications */
    @dataType(Request)
    request: Request;

    /** The description of containers, databases, services */
    @skip()
    description: Object;
}