import { Injectable } from '@angular/core';
import { Request } from '../model/Request';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { DatabaseConfiguration } from '../model/database/DatabaseConfiguration';
import { TemplateResponse } from '../model/TemplateResponse';
import deserializer from 'angular-http-deserializer';
import { map } from 'rxjs/operators';
import { ImageAnalysis } from '../model/ImageAnalysis';
import { KubernetesDeployRequest } from '../model/KubernetesDeployRequest';
import { KubernetesDeployResponse } from '../model/KubernetesDeployResponse';

@Injectable({
  providedIn: 'root'
})
/** Class which is responsible for the communication with the backend */
export class RequestService {
  /** Address of the backend */
  serviceUrl: string = `http://${location.host}`;

  /** Header for the request */
  headers = new HttpHeaders({'Content-Type':'application/json'});

  constructor(private http: HttpClient) { }

  /**
   * Sends a request to the backend and gets the corresponding deployment file
   * 
   * @param request The request which should be sent
   * @returns A Kubernetes YAML string
   */
  getDeploymentFile(request: Request): Observable<string> {
    var requestCopy = JSON.parse(JSON.stringify(request))

    this.encodeEnvironments(requestCopy);
    
    return this.http.post<string>(this.serviceUrl + '/generate',requestCopy,{headers:this.headers, responseType: 'text' as 'json'});
  }

  /**
   * Fetches all available (or supported) databases from the backend
   * 
   * @returns An array containing all supported databases by the backend
   */
  getAvailableDatabases(): Observable<DatabaseConfiguration[]> {
    return this.http.get<DatabaseConfiguration[]>(this.serviceUrl + "/getAvailableDatabases");
  }

  /**
   * Proxies the search for a image from dockerhub to the backend
   * 
   * @param image Image which should be searched on dockerhub
   * @returns A string array containing search results
   */
  getImageFromDockerHub(image: string): Observable<string[]> {
    if (image.trim() === '') {
      return of([]);
    }

    return this.http.get<string[]>(`${this.serviceUrl}/image`, {params: new HttpParams().set('image',image)});
  }

  /**
   * Gets all available templates from the backend
   * 
   * @returns A string array containing all available templates
   */
  getTemplates(): Observable<string[]> {
    return this.http.get<string[]>(`${this.serviceUrl}/templates`);
  }

  /**
   * Gets a specific template from the backend
   * 
   * @param name The name of the template
   * @returns A TemplateResponse consisting of Kubernetes YAML file, Request for modification and Description for the request
   */
  getTemplate(name: string): Observable<TemplateResponse> {
    return this.http.get<TemplateResponse>(`${this.serviceUrl}/templates/${name}`).pipe(map(deserializer<TemplateResponse>(TemplateResponse)));
  }

  /**
   * Gets an image analysis to a specific image
   * 
   * @param image The name of the image
   * @returns An ImageAnalysis containing ports, environment variables and mount points
   */
  getImageAnalysis(image: string): Observable<ImageAnalysis> {
    return this.http.get<ImageAnalysis>(`${this.serviceUrl}/analyzeImage`, {params: new HttpParams().set('image',image)});
  }

  /**
   * Deploys a deployment file in Kubernetes
   * 
   * @param request The Deployment request with address, token and deployment
   * @returns A KubernetesDeployResponse containing the status of the Deployment
   */
  deployYAMLFile(request: KubernetesDeployRequest): Observable<KubernetesDeployResponse> {
    return this.http.post<KubernetesDeployResponse>(`${this.serviceUrl}/deploy`,request,{headers: this.headers});
  }

  /**
   * Deletes a deployment file in Kubernetes
   * 
   * @param request The Deployment request with address, token and deployment
   * @returns A KubernetesDeployResponse containing the status of the Deletion
   */
  deleteYAMLFile(request: KubernetesDeployRequest) : Observable<KubernetesDeployResponse> {
    return this.http.post<KubernetesDeployResponse>(`${this.serviceUrl}/deleteDeployment`,request,{headers: this.headers});
  }

  /**
   * Fetches all externally accessible Service Endpoints of the deployment
   * 
   * @param request The Deployment request with address, token and deployment
   * @returns A KubernetesDeployResponse containing the status of the fetching process and a map containing all service endpoints
   */
  getEndPoints(request: KubernetesDeployRequest) : Observable<KubernetesDeployResponse> {
    return this.http.post<KubernetesDeployResponse>(`${this.serviceUrl}/getDeploymentStatus`,request,{headers: this.headers});
  }

  /**
   * Encodes secret variables with Base64
   * 
   * @param requestCopy A copy of the request where all secret environment variables get encoded with Base64
   */
  private encodeEnvironments(requestCopy: Request): void {
    for (var i = 0; i < requestCopy.containers.length; i++) {
      var container = requestCopy.containers[i];

      for (var j = 0; j < container.environments.length; j++) {
        if (container.environments[j].value !== null && container.environments[j].secret) {
          container.environments[j].value = btoa(container.environments[j].value);
        }
      }
    }

    for (var i = 0; i < requestCopy.databases.length; i++) {
      var database = requestCopy.databases[i];

      for (var j = 0; j < database.environments.length; j++) {
        if (database.environments[j].secret) {
          database.environments[j].value = btoa(database.environments[j].value);
        }
      }
    }
  }
}
