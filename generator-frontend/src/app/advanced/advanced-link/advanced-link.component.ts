import { Component, OnInit, Input } from '@angular/core';
import { ControlContainer, NgForm} from '@angular/forms';
import { Request } from '../../model/Request';
import { BaseContainer } from '../../model/BaseContainer';
import { Link } from '../../model/Link';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../../animations/animations';

@Component({
  selector: 'app-advanced-link',
  templateUrl: './advanced-link.component.html',
  styleUrls: ['./advanced-link.component.css'],
  viewProviders: [{provide: ControlContainer, useExisting: NgForm}],
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
 * Class which is necessary to render links in advanced mode
 */
export class AdvancedLinkComponent implements OnInit {
  /** The request for getting the list of links */
  @Input()
  request: Request

  /** Wheter the form was already submitted or not */
  @Input()
  submitted: boolean;

  /** The description for templates (environment variables, ports, storages, etc.) */
  @Input()
  description: Object;

  constructor() { }

  ngOnInit() {
  }

  /**
   * Adds a link to the request
   */
  addLink(): void {
    this.request.links.push(new Link());
  }

  /**
   * Deletes a link from the request
   * 
   * @param link The link which should get deleted
   */
  deleteLink(link: Link): void {
    this.request.links = this.request.links.filter(l => l !== link);
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
   * Extracts the description of a link from the source containers description
   * 
   * @param linkName Name of the link (most likely name of the environment variable)
   * @param sourceContainer Unique name of the source container
   * @returns The link description or null if no description for the given link exists
   */
  getLinkDescription(linkName: string, sourceContainer: string): Object {
    if (linkName === null || sourceContainer === null || this.description === null) {
      return null;
    }

    sourceContainer = this.getContainerName(sourceContainer);

    if (sourceContainer === null) {
      return null;
    }

    for (let key of Object.keys(this.description)) {
      var obj = this.description[key];

      if (obj['name'] === sourceContainer) {
        if (obj['env'] !== null && obj['env'] !== undefined) {
          var envDescription = obj['env'];

          for (let linkKey of Object.keys(envDescription)) {
            var linkObj = envDescription[linkKey];

            if (linkObj['name'] === linkName) {
              return linkObj;
            }
          }
        } 
      }
    }

    return null;
  }

  /**
   * Gets the Container name from the unique name of a container
   * 
   * @param uniqueName The unique name of a container
   * @returns The name of the container
   */
  getContainerName(uniqueName: string): string {
    for (let container of this.request.containers) {
      if (container.uniqueName === uniqueName) {
        return container.name;
      }
    }

    return null;
  }
}
