import { Component, OnInit, Input } from '@angular/core';
import { ControlContainer, NgForm } from '@angular/forms';
import { Request } from '../../model/Request';
import { Service } from '../../model/service/Service';
import { Port } from '../../model/Port';
import { BaseContainer } from '../../model/BaseContainer';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../../animations/animations';

@Component({
  selector: 'app-advanced-service',
  templateUrl: './advanced-service.component.html',
  styleUrls: ['./advanced-service.component.css'],
  viewProviders: [{ provide: ControlContainer, useExisting: NgForm }],
  animations: [
    trigger(
      'enterAnimation', [
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
    )
  ]
})
/**
 * Class which is necessary to render services in advanced mode
 */
export class AdvancedServiceComponent implements OnInit {
  /** The request for getting the list of services */
  @Input()
  request: Request;

  /** Wheter the form got already submitted or not */
  @Input()
  submitted: boolean;

  /** The description for templates */
  @Input()
  description: Object;

  constructor() { }

  ngOnInit() {
  }

  /**
   * Adds a service to the request
   */
  addService(): void {
    this.request.services.push(new Service());
  }

  /**
   * Deletes a service from the request
   * 
   * @param service The service which should be deleted from the request
   */
  deleteService(service: Service): void {
    this.request.services = this.request.services.filter(s => s !== service);
  }

  /**
   * Searches the corresponding container for the service and copies all it's ports
   * 
   * @param service The service for which the ports should get copied from it's container
   */
  getPortsForService(service: Service) {
    let container = null;

    for (let i = 0; i < this.request.containers.length && container === null; i++) {
      if (this.request.containers[i].uniqueName === service.normalServiceOptions.containerName) {
        container = this.request.containers[i];
      }
    }

    if (container === null) {
      return [];
    }

    let result = [];
    for (let i = 0; i < container.ports.length; i++) {
      let port = new Port();
      port.name = container.ports[i].name;
      port.port = container.ports[i].port;
      port.protocol = container.ports[i].protocol;
      port.targetPort = container.ports[i].targetPort;

      result.push(port);
    }

    service.normalServiceOptions.ports = result;
  }

  /**
   * Searches a container and returns all it's ports
   * 
   * @param container The unique name of the container
   * @returns An array containing all the containers ports
   */
  getPortsForContainer(container: string): Port[] {
    for (let i = 0; i < this.request.containers.length; i++) {
      if (this.request.containers[i].uniqueName === container) {
        return this.request.containers[i].ports;
      }
    }

    return null;
  }

  /**
   * Returns all containers and databases
   * 
   * @returns An array containing all containers and databases
   */
  getAllContainer(): BaseContainer[] {
    return (<BaseContainer[]>this.request.containers).concat(<BaseContainer[]>(this.request.databases));
  }

  /**
   * Searches the description for a service
   * 
   * @param name The name of the service
   * @returns A JSON Object containing the description of the service
   */
  getDescriptionForService(name: string) : Object {
    if (this.description === null || name === null || this.description === undefined || name === undefined) {
      return null;
    }

    for (let key of Object.keys(this.description)) {
      var obj = this.description[key];

      if (obj['name'] === name) {
        return obj;
      }
    }

    return null;
  }

  /**
   * Returns a specific name of the JSON description object
   * 
   * @param obj The JSON description object
   * @param name The name for the object which should get a description
   */
  getObjectFromDescription(obj: Object, name: string): Object {
    if (obj === null || obj === undefined || name === null || name === undefined) {
      return null;
    }

    for (let key of Object.keys(obj)) {
      var val = obj[key];

      if (val['name'] === name) {
        return val;
      }
    }

    return null;
  }

  /**
   * Changes the name of the corresponding service description, if the service name changes
   * 
   * @param name The new name of the service
   * @param description The description of the service
   */
  changeServiceDescription(name: string, description: Object) {
    if (name === null || description === null) {
      return;
    }

    description['name'] = name;
  }
}
