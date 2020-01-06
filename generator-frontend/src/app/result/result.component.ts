import { Component, OnInit, Input } from '@angular/core';
import { Request } from '../model/Request';
import { RequestService } from '../services/request.service';
import { KubernetesDeployRequest } from '../model/KubernetesDeployRequest';
import { KubernetesDeployResponse } from '../model/KubernetesDeployResponse';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../animations/animations';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css'],
  animations: [
    trigger(
      'fastEnterAnimation', [
      transition(':enter', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '0',
            endHeight: '*',
            time: '250'
          }
        }
        )
      ]),
      transition(':leave', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '*',
            endHeight: '0',
            time: '250'
          }
        }
        )
      ])
    ]
    ),
    trigger(
      'enterAnimation', [
      transition(':enter', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '0',
            endHeight: '*',
            time: '500'
          }
        }
        )
      ]),
      transition(':leave', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '*',
            endHeight: '0',
            time: '500'
          }
        }
        )
      ])
    ]
    )
  ]
})
/** Class for displaying the result page */
export class ResultComponent implements OnInit {
  /** The Kubernetes YAML file */
  @Input()
  deployment: string;

  /** The request for sending users back to advanced mode */
  @Input()
  request: Request;

  /** The description of a request (in case the user requested a templates) */
  @Input()
  description: Object;

  /** Address of Kubernetes Master */
  deployAddress: string;

  /** Kubernetes Cluster Admin Token */
  deployToken: string;

  /** Whether to deploy, delete or Get Service Endpoints of a Deploymnet */
  deployType: string;

  /** Message for the Alert Box */
  deployAlertMessage: string;

  /** Type of the alert */
  deployAlertType: string;

  /** Subject for the Alertbox (needed for autoclosing) */
  deploySubject: Subject<string> = new Subject<string>();

  /** Endpoints of the deployment */
  deployEndpoints: Map<string, string[]>;

  constructor(private requestService: RequestService) { }

  /**
   * Initizalizes the Subject of the alertbox.
   */
  ngOnInit() {
    this.deploySubject.subscribe(m => this.deployAlertMessage = m);
    this.deploySubject.pipe(debounceTime(5000)).subscribe(() => this.deployAlertMessage = null);
  }

  /**
   * Copies the message to the users clipboard
   */
  copyMessage(): void {
    let selBox = document.createElement('textarea');
    selBox.style.position = 'fixed';
    selBox.style.left = '0';
    selBox.style.top = '0';
    selBox.style.opacity = '0';
    selBox.value = this.deployment;
    document.body.appendChild(selBox);
    selBox.focus();
    selBox.select();
    document.execCommand('copy');
    document.body.removeChild(selBox);
  }

  /** Opens a download dialog and lets the user download the deployment as YAML file */
  download() {
    var element = document.createElement('a');
    element.setAttribute('href', `data:text/plain;charset=utf-8,${encodeURIComponent(this.deployment)}`);
    element.setAttribute('download', "deployment.yaml");

    var event = new MouseEvent("click");
    element.dispatchEvent(event);
  }

  /**
   * Method for deploying, deleting or getting all endpoints for a deployment
   */
  deployOrDeleteForm() {
    var request = new KubernetesDeployRequest(this.deployAddress, this.deployToken, this.deployment);
    this.deployAlertMessage = '<i class="fas fa-info"></i> <b>Info:</b> Action in Progress!'
    this.deployAlertType = 'info';

    if (this.deployType === 'DEPLOY') {
      this.requestService.deployYAMLFile(request).subscribe(c => this.displayAlert(c), err => console.log(err));
    }

    if (this.deployType === 'DELETE') {
      this.requestService.deleteYAMLFile(request).subscribe(c => this.displayAlert(c), err => console.log(err));
      this.deployEndpoints = null;
    }

    if (this.deployType === 'ENDPOINTS') {
      this.requestService.getEndPoints(request).subscribe(c => this.displayAlertAndShowEndpoints(c), err => console.log(err));
    }
  }

  /**
   * Displays an alert and shows the deployment endpoints, when a user requested all deployment endpoints
   * 
   * @param response The response from the backend with all service endpoints
   */
  displayAlertAndShowEndpoints(response: KubernetesDeployResponse) {
    if (response.status === 'Success') {
      this.deployAlertType = 'success';

      var map = new Map<string, string[]>();

      for (let key of Object.keys(response.serviceEndpoints)) {
        map.set(key, response.serviceEndpoints[key]);
      }

      this.deployEndpoints = map;

      var message = `<i class="fas fa-check-circle"></i> <b>Success!</b> ${response.message}`;
      if (this.deployEndpoints.size === 0) {
        message += ` <b>No Service Endpoints found</b>`
      }

      this.deploySubject.next(message);
    }

    if (response.status === 'Error') {
      this.deployAlertType = 'danger';

      this.deploySubject.next(`<i class="fas fa-times"></i> <b>Error!</b> ${response.message}`);
      this.deployEndpoints = null;
    }
  }

  /**
   * Gets all Keys from the service endpoint map
   * 
   * @returns An array containing all keys of the map
   */
  getEndpointKeys(): string[] {
    if (this.deployEndpoints === null || this.deployEndpoints.size === 0) {
      return [];
    }

    return Array.from(this.deployEndpoints.keys());
  }

  /**
   * Gets all values for a key and returns them as a single string
   * 
   * @param key The key for which all values should be returned
   * @returns A string containg all values of the key
   */
  getEndpointValueString(key: string): string {
    if (this.deployEndpoints === null || this.deployEndpoints.size === 0) {
      return null;
    }

    var array = this.deployEndpoints.get(key);
    if (array === null || array.length === 0) {
      return null;
    }

    var result = "";
    for (let value of array) {
      result += `${value}<br />`;
    }

    return result;
  }

  /**
   * Displays a simple alert for deploying or deleting a deployment
   * 
   * @param response The response from the backend containing the results
   */
  displayAlert(response: KubernetesDeployResponse) {
    if (response.status === 'Success') {
      this.deployAlertType = 'success';

      this.deploySubject.next(`<i class="fas fa-check-circle"></i> <b>Success!</b> ${response.message}`);
    }

    if (response.status === 'Warning') {
      this.deployAlertType = 'warning';

      this.deploySubject.next(`<i class="fas fa-exclamation"></i> <b>Warning!</b> ${response.message}`);
    }

    if (response.status === 'Error') {
      this.deployAlertType = 'danger';

      this.deploySubject.next(`<i class="fas fa-times"></i> <b>Error!</b> ${response.message}`);
    }
  }
}
