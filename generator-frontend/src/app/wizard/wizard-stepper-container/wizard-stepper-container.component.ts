import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { MainWizardOptions } from '../../model/wizard/MainWizardOptions';
import { Service } from '../../model/service/Service';
import { Subject, Observable, of } from 'rxjs';
import { RequestService } from '../../services/request.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { Container } from '../../model/container/Container';
import { ContainerWizardOptions } from '../../model/wizard/ContainerWizardOptions';
import { debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { Request } from '../../model/Request';
import { Port } from '../../model/Port';
import { Probe } from '../../model/container/Probe';
import { ImageAnalysis } from '../../model/ImageAnalysis';
import { Storage } from '../../model/container/Storage';
import { EnvironmentVariable } from '../../model/container/EnvironmentVariable';
import { WizardComponent, MovingDirection } from 'angular-archwizard';
import { NgForm } from '@angular/forms';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../../animations/animations';

@Component({
  selector: 'app-wizard-stepper-container',
  templateUrl: './wizard-stepper-container.component.html',
  styleUrls: ['./wizard-stepper-container.component.css'],
  animations: [
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
    ),
    trigger(
      "blockInitialRenderAnimation",
      [
        transition(":enter", [])
      ]
    )
  ]
})
export class WizardStepperContainerComponent implements OnInit {

  /** Global wizard options */
  @Input()
  mainWizardOptions: MainWizardOptions;

  /** Request which gets created */
  @Input()
  request: Request;

  /** Container which currently is being modified */
  @Input()
  container: Container;

  /** Service corresponding to the container */
  service: Service = null;

  /** If the form was already submitted or not */
  submitted: boolean = false;

  /** Submit Type for getting next page */
  submitType: string;

  /** Image Query for debouncing the image analysis */
  imageQuery: Subject<any> = new Subject<any>();

  @ViewChild(WizardComponent, {static: true})
  public wizard: WizardComponent;

  @ViewChild('nameForm', {static: true})
  nameForm: NgForm;

  xlOpen = true // For openning Clarity Wizard

  constructor(private requestService: RequestService, private spinner: NgxSpinnerService) { }

  /**
   * Initializes the component.
   * If there is no container it creates one.
   * Initializes image query for debouncing image analysis
   */
  ngOnInit() {
    if (this.container === null || this.container === undefined) {
      this.container = new Container();
      this.container.wizardOptions = new ContainerWizardOptions();
    } else {
      this.service = this.findService();
    }

    this.imageQuery.pipe(debounceTime(1000), distinctUntilChanged((prev, curr) => prev.image === curr.image)).subscribe(e => {
      this.getImageAnalysis(e.container, e.image);
    });
  }

  moveBackwards(direction: MovingDirection): boolean {
    if (direction === MovingDirection.Backwards) {
      return true;
    }

    return false;
  }

  log(any: any) {
    console.log(any);
  }

  resetForm(form: NgForm) {
    (form as {submitted: boolean}).submitted = false;
  }

  /**
  * Finds the corresponding service for the current container
  * 
  * @returns The service if one service for the container exists or null otherwise
  */
  findService(): Service {
    for (let service of this.request.services) {
      if (service.normalServiceOptions.containerName === this.container.uniqueName) {
        return service;
      }
    }

    return null;
  }

  /**
   * Adds or deletes a port from a service, depending on the expose checkbox
   * 
   * @param port The port which should be exposed
   * @param event The Checkbox event
   */
  changePortService(port: Port, event: Event) {
    if (this.service === null) {
      this.service = new Service();
      this.service.name = `${this.container.name}-externalservice`;
      this.service.normalServiceOptions.containerName = this.container.uniqueName;
    }

    var element = <HTMLInputElement>event.currentTarget;
    if (element.checked) {
      this.service.normalServiceOptions.ports.push(JSON.parse(JSON.stringify(port)));
    } else {
      this.deleteServicePort(port);
    }
  }

  /**
   * Changes the port on modification if its a service port
   * 
   * @param port The port that has changed
   */
  changePortInService(port: Port) {
    if (this.service === null) {
      return;
    }

    for (let servicePort of this.service.normalServiceOptions.ports) {
      if (port.uniqueName === servicePort.uniqueName) {
        servicePort.port = port.port;
        servicePort.protocol = port.protocol;
        return;
      }
    }
  }

  /**
   * Deletes a port from the service and deletes the service if it containts no other ports
   * 
   * @param port The port that should be deleted from the service
   */
  deleteServicePort(port: Port) {
    if (this.service === null) {
      return;
    }

    this.service.normalServiceOptions.ports = this.service.normalServiceOptions.ports.filter(p => p.uniqueName !== port.uniqueName);

    if (!this.service.normalServiceOptions.ports.length) {
      this.request.services = this.request.services.filter(s => s !== this.service);
      this.service = null;
    }
  }

  /**
   * Changes the service name if the name of the container changes
   */
  adaptServiceName(): void {
    if (this.service === null) {
      return;
    }

    this.service.name = `${this.container.name}-externalService`;
  }

  /**
   * Deletes the port and checks for Probe dependencies
   * 
   * @param port The port that should be deleted
   */
  deletePort(port: Port) {
    this.container.ports = this.container.ports.filter(c => c !== port);

    if (this.container.containerAdvancedOptions.readinessProbe != null) {
      if (this.container.containerAdvancedOptions.readinessProbe.httpPort === port.port) {
        this.container.containerAdvancedOptions.readinessProbe.httpPort = null;
      }

      if (this.container.containerAdvancedOptions.readinessProbe.tcpPort === port.port) {
        this.container.containerAdvancedOptions.readinessProbe.tcpPort = null;
      }
    }

    this.deleteServicePort(port);
  }

  /**
   * Checks if the port is exposed with a service
   * 
   * @param port The port that should be checked
   * @returns true if the service contains the port and false otherwise
   */
  isPortExported(port: Port): boolean {
    if (this.service === null) {
      return false;
    }

    for (let servicePort of this.service.normalServiceOptions.ports) {
      if (servicePort.uniqueName === port.uniqueName) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks container for duplicate ports and keeps a single instance
   */
  checkForDuplicatePorts() {
    var ports = [];
    var servicePorts = [];

    for (let port of this.container.ports) {
      var contains = false;

      for (var i = 0; i < ports.length && !contains; i++) {
        if (port.port === ports[i].port) {
          contains = true;
        }
      }

      if (!contains) {
        var exposedPort = this.getExposedPort(port);

        if (exposedPort !== null) {
          ports.push(exposedPort);
          servicePorts.push(JSON.parse(JSON.stringify(exposedPort)));
        } else {
          ports.push(port);
        }
      }
    }

    this.container.ports = ports;
    if (this.service !== null) {
      this.service.normalServiceOptions.ports = servicePorts;
    }
  }

  addContainer() {
    this.checkForDuplicatePorts();
    //this.request.containers.push(this.container);
    this.submitted = false;
    this.container = new Container();
    this.container.wizardOptions = new ContainerWizardOptions();

    this.wizard.reset();
    this.nameForm.reset();
  }

  /**
   * Switches the wizard to the next page
   */
  getNextPage() {
    if (this.submitType === 'CONTINUE') {
      this.checkForDuplicatePorts();
      this.request.containers.push(this.container);

      if (this.service !== null) {
        this.request.services.push(this.service);
      }
    }

    if (this.submitType === 'ADD') {
      this.checkForDuplicatePorts();
      this.request.containers.push(this.container);
      this.submitted = false;
      this.container = new Container();
      this.container.wizardOptions = new ContainerWizardOptions();

      if (this.service !== null) {
        this.request.services.push(this.service);
        this.service = null;
      }

      this.wizard.reset();
      this.nameForm.reset();
    }
    if (this.submitType === 'START') 
    {
      this.mainWizardOptions.page = 'START';
    }

    if (this.submitType === 'RESULT') {
      this.checkForDuplicatePorts();
      this.mainWizardOptions.page = 'RESULT';

      if (this.service !== null) {
        for (let servicePort of this.request.services) {
          if (servicePort === this.service) {
            return;
          }
        }

        this.request.services.push(this.service);
      }
    }

    if (this.submitType === 'SKIP' || this.submitType === 'CONTINUE') {
      var nextPage = 'LINK';

      for (var i = 0; i < this.request.containers.length && nextPage === 'LINK'; i++) {
        if (this.request.containers[i].wizardOptions.databaseStore) {
          nextPage = 'DATABASE';
        }
      }

      this.mainWizardOptions.page = nextPage;
    }
  }

  /**
   * Checks if a exposed port with the current port exist
   * 
   * @param port The port that should be checkd
   * @returns A port that is exposed and has the same value or null if no exposed port exist
   */
  getExposedPort(port: Port): Port {
    if (this.service === null) {
      return null;
    }

    for (let servicePort of this.service.normalServiceOptions.ports) {
      if (port.port === servicePort.port) {
        return servicePort;
      }
    }

    return null;
  }


  /**
   * Resets the input of the container form and creates a new Container
   */
  resetContainer() {
    this.request.containers = this.request.containers.filter(c => c !== this.container);
    this.container = new Container();
    this.container.wizardOptions = new ContainerWizardOptions();
    this.service = null;
    
    this.wizard.reset();
    this.nameForm.reset();
  }

  /**
   * Resets the storage and sets replicas back  to 1
   */
  resetStorage() {
    this.container.replicas = 1;
    this.container.storage = [];
  }

  /**
   * Resets all environment variables
   */
  resetEnvironmentVariables() {
    this.container.environments = [];
  }

  /**
   * Resets all init containers
   */
  resetInitContainers() {
    this.container.containerAdvancedOptions.initContainers = [];
  }

  /**
   * Resets the readiness probe
   */
  resetProbe() {
    if (this.container.wizardOptions.probeConfigurable === true) {
      this.container.containerAdvancedOptions.readinessProbe = new Probe(3, 30, 10, 1, 1);
      this.container.containerAdvancedOptions.gracePeriod = 15;
    } else {
      this.container.containerAdvancedOptions.readinessProbe = null;
    }
  }

  /**
   * Resets the http action and port or the tcp port
   */
  resetHttp() {
    if (this.container.wizardOptions.httpConfigurable === true) {
      this.container.containerAdvancedOptions.httpService = true;
      this.container.containerAdvancedOptions.readinessProbe.tcpPort = null;
    } else {
      this.container.containerAdvancedOptions.httpService = false;
      this.container.containerAdvancedOptions.readinessProbe.httpAction = null;
      this.container.containerAdvancedOptions.readinessProbe.httpPort = 80;
    }

    let found = false;
    for (let i = 0; i < this.container.ports.length && !found; i++) {
      if (this.container.ports[i].port === 80) {
        found = true;
      }
    }

    this.container.containerAdvancedOptions.readinessProbe.httpPort = (found ? 80 : null);
  }

  /** Puts next entry in the subject for the image analysis */
  analyzeImageDebounce(query: string) {
    this.imageQuery.next({ image: query, container: this.container });
  }

  /**
   * Fetches the image analysis from the backend and sets the analysis for the container
   * 
   * @param container The container for which the image should get analyzed
   * @param image The name of the image which should get analyzed
   */
  getImageAnalysis(container: Container, image: string) {
    container.analysis = null;
    if (image === null || image === undefined || image.trim() === '') {
      container.loadingAnalysis = false;
      this.spinner.hide()
      return;
    }

    this.spinner.show()
    container.loadingAnalysis = true;
    this.requestService.getImageAnalysis(image).subscribe(e => {
      this.setImageAnalysis(container, e);
    },
      () => this.abortImageAnalysis(container, image));
  }

  /**
   * Sets the image analysis for the container, if it is the currently desired image
   * 
   * @param container The container for which an image analysis was requested
   * @param analysis An image analysis for an image
   */
  setImageAnalysis(container: Container, analysis: ImageAnalysis) {
    if (container.image === undefined || container.image === null || container.image.trim() === '') {
      return;
    }

    var checkname;
    if (container.image.indexOf(':') === -1) {
      checkname = `${container.image}:latest`
    } else {
      checkname = container.image;
    }

    if (checkname === analysis.name) {
      container.analysis = analysis;
      container.loadingAnalysis = false;
      this.spinner.hide()
    }
  }

  /**
   * In case fetching the image analysis fails this aborts the loading process for the container, if it's the currently selected image
   * 
   * @param container The container for which an image analysis was requested
   * @param image The name of the image
   */
  abortImageAnalysis(container: Container, image: string) {
    console.log(`Could not analyze image ${image}`);
    if (container.image === null || container.image === undefined || container.image.trim() === '') {
      container.loadingAnalysis = false;
      this.spinner.hide();
    }

    if (container.image === image) {
      container.loadingAnalysis = false;
      this.spinner.hide();
    }
  }

  /**
   * Retrieves all mount paths from the image analysis and adds them to the container
   */
  appendMountPathsFromImage() {
    if (this.container.analysis === undefined || this.container.analysis === null || this.container.analysis.mountPaths === undefined || this.container.analysis.mountPaths === null || this.container.analysis.mountPaths.length === 0) {
      return;
    }

    for (let mountPath of this.container.analysis.mountPaths) {
      var insert = true;

      for (var i = 0; i < this.container.storage.length && insert; i++) {
        var storage = this.container.storage[i];

        for (var j = 0; j < storage.storageMounts.length && insert; j++) {
          if (storage.storageMounts[i].mountPath === mountPath) {
            insert = false;
          }
        }
      }

      if (insert) {
        var storage = new Storage();
        storage.storageMounts[0].mountPath = mountPath;

        this.container.storage.push(storage);
      }

    }
  }

  /**
   * Retrieves all environment variables from the image analysis and adds them to the container
   */
  appendVariablesFromImage() {
    if (this.container.analysis === undefined || this.container.analysis === null || this.container.analysis.environments === undefined || this.container.analysis.environments === null || this.container.analysis.environments.length === 0) {
      return;
    }

    for (let variable of this.container.analysis.environments) {
      var insert = true;

      for (var i = 0; i < this.container.environments.length && insert; i++) {
        if (this.container.environments[i].name === variable.name) {
          insert = false;
        }
      }

      if (insert) {
        var envVar = new EnvironmentVariable();
        envVar.name = variable.name;
        envVar.value = variable.value;

        this.container.environments.push(envVar);
      }

    }
  }

  /**
   * Retrieves all ports from the image analysis and adds them to the container
   */
  appendPortsFromImage() {
    if (this.container.analysis === undefined || this.container.analysis === null || this.container.analysis.ports === undefined || this.container.analysis.ports === null || this.container.analysis.ports.length === 0) {
      return;
    }

    for (let analysisPort of this.container.analysis.ports) {
      var insert = true;

      for (var i = 0; i < this.container.ports.length && insert; i++) {
        if (this.container.ports[i].port === analysisPort.port) {
          insert = false;
        }
      }

      if (insert) {
        var port = new Port();
        port.port = analysisPort.port;
        port.protocol = analysisPort.protocol;

        this.container.ports.push(port);
      }

    }
  }

  /** Image search on docker hub */
  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term =>
        this.requestService.getImageFromDockerHub(term).pipe(
          catchError(() => {
            return of([]);
          }))
      )
    )

}
