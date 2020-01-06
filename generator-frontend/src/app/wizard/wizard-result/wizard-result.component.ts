import { Component, OnInit, Input } from '@angular/core';
import { MainWizardOptions } from 'src/app/model/wizard/MainWizardOptions';
import { Request } from 'src/app/model/Request';
import { Container } from 'src/app/model/container/Container';
import { Link } from 'src/app/model/Link';
import { Database } from 'src/app/model/database/Database';
import { BaseContainer } from 'src/app/model/BaseContainer';
import { RequestService } from 'src/app/services/request.service';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from 'src/app/animations/animations';

@Component({
  selector: 'app-wizard-result',
  templateUrl: './wizard-result.component.html',
  styleUrls: ['./wizard-result.component.css'],
  animations: [
    trigger(
      "blockInitialRenderAnimation",
      [
        transition(":enter", [])
      ]
    ),
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
    )
  ]
})
/** Class which is necessary for displaying the wizard result page */
export class WizardResultComponent implements OnInit {
  /** Request which gets created */
  @Input()
  request: Request;

  /** Global wizard options */
  @Input()
  mainWizardOptions: MainWizardOptions;

  constructor(private requestService: RequestService) { }

  ngOnInit() {
  }

  /**
   * Creates a new container and switches to container page
   */
  addContainer() {
    this.mainWizardOptions.currentContainer = null;
    this.mainWizardOptions.editContainer = false;
    this.mainWizardOptions.addContainer = true;
    this.mainWizardOptions.page = 'CONTAINER';
    window.scroll({top: 0, left: 0, behavior: "smooth"});
  }

  /**
   * Edits a container and switches to container page
   * 
   * @param container The container which should get edited
   */
  editContainer(container: Container) {
    this.mainWizardOptions.currentContainer = container;
    this.mainWizardOptions.editContainer = true;
    this.mainWizardOptions.addContainer = false;
    this.mainWizardOptions.page = 'CONTAINER';
    window.scroll({top: 0, left: 0, behavior: "smooth"});
  }

  /**
   * Deletes a container form the request and all corresponding links and services
   * 
   * @param container The container which should get deleted
   */
  deleteContainer(container: Container) {
    this.request.containers = this.request.containers.filter(c => c !== container);
    this.deleteService(container.uniqueName);
    this.checkLinkDependencies(container);
  }

  /**
   * Creates a new database and switches to database page
   */
  addDatabase() {
    this.mainWizardOptions.currentDatabase = null;
    this.mainWizardOptions.editDatabase = false;
    this.mainWizardOptions.addDatabase = true;
    this.mainWizardOptions.page = 'DATABASE';
    window.scroll({top: 0, left: 0, behavior: "smooth"});
  }

  /**
   * Edits a database and switches to database page
   * 
   * @param database The database which should get edited
   */
  editDatabase(database: Database) {
    this.mainWizardOptions.currentDatabase = database;
    this.mainWizardOptions.editDatabase = true;
    this.mainWizardOptions.addDatabase = false;
    this.mainWizardOptions.page = 'DATABASE';
    window.scroll({top: 0, left: 0, behavior: "smooth"});
  }

  /**
   * Deletes a database and all corresponding links from the database
   * 
   * @param database The database which should get deleted
   */
  deleteDatabase(database: Database) {
    this.request.databases = this.request.databases.filter(d => d !== database);
    this.checkLinkDependencies(database);
  }

  /**
   * Creates a link and switches to the EDIT_LINK page
   */
  addLink() {
    this.mainWizardOptions.currentLink = null;
    this.mainWizardOptions.addLink = true;
    this.mainWizardOptions.page = 'EDIT_LINK';
  }

  /**
   * Edits a link and switches to the EDIT_LINK page
   * 
   * @param link The link which should get edited
   */
  editLink(link: Link) {
    this.mainWizardOptions.currentLink = link;
    this.mainWizardOptions.page = 'EDIT_LINK';
    this.mainWizardOptions.addLink = false;
  }

  /**
   * Deletes a link from the request
   * 
   * @param link The link that should get deleted
   */
  deleteLink(link: Link) {
    this.request.links = this.request.links.filter(l => l !== link);
  }

  /**
   * Deletes a service from the request
   * 
   * @param serviceName The name of the service which should get deleted
   */
  deleteService(serviceName: string) {
    let service;

    for (let servicePort of this.request.services) {
      if (servicePort.normalServiceOptions.containerName === serviceName) {
        service = servicePort;
      }
    }

    this.request.services = this.request.services.filter(s => s !== service);
  }

  /**
   * Submits the request to the backend and displays the YAML file on success
   */
  submit() {
    this.requestService.getDeploymentFile(this.request).subscribe(content => this.mainWizardOptions.deployment = content,
      err => console.log(err),
      () => this.mainWizardOptions.page = 'YAML');
  }

  /**
   * Checks if the link addresses a deleted container and deletes the corresponding link
   * 
   * @param container The container that was deleted
   */
  checkLinkDependencies(container: BaseContainer) {
    this.request.links = this.request.links.filter(l => l.containerFrom !== container.uniqueName && l.containerTo !== container.uniqueName);
  }

  /**
   * Creates the link description that is shown in the edit link button
   * 
   * @param link The link for which the description should get created
   */
  getLinkDescription(link: Link) : string {
    let containerFrom = null;
    let containerTo = null;

    for (let container of this.request.containers) {
      if (container.uniqueName === link.containerFrom) {
        containerFrom = container;
      }
      if (container.uniqueName === link.containerTo) {
        containerTo = container;
      }
    }

    for (let database of this.request.databases) {
      if (database.uniqueName === link.containerFrom) {
        containerFrom = database;
      }
      if (database.uniqueName === link.containerTo) {
        containerTo = database;
      }
    }

    var variable;
    if (link.scheme !== null && link.scheme !== undefined && link.scheme.indexOf("<link>") >= 0) {
      variable = link.scheme.replace("<link>",containerTo.name);
    } else {
      variable = containerTo.name;
    }

    return `Edit Link from Container ${containerFrom.name}: ${link.name}=${variable}`;
  }
}
