import { Component, OnInit, Input } from '@angular/core';
import { ControlContainer, NgForm } from '@angular/forms'
import { Request } from '../../model/Request';
import { Container } from '../../model/container/Container';
import { debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { Observable, of, Subject, pipe } from 'rxjs';
import { RequestService } from '../../services/request.service';
import { ImageAnalysis } from '../../model/ImageAnalysis';
import { Port } from '../../model/Port';
import { EnvironmentVariable } from '../../model/container/EnvironmentVariable';
import { Storage } from '../../model/container/Storage';
import { StorageMount } from '../../model/container/StorageMount';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from '../../animations/animations';
import {MatDialog, MatDialogModule, MatBottomSheet, MatBottomSheetRef} from '@angular/material'

@Component({
  selector: 'app-advanced-container',
  templateUrl: './advanced-container.component.html',
  styleUrls: ['./advanced-container.component.css'],
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
 * Class which is necessary to render containers in advanced mode
 */
export class AdvancedContainerComponent implements OnInit {
  
  
  /** The request for getting the list of containers */
  @Input()
  request: Request;

  /** Wheter the form was already submitted or not */
  @Input()
  submitted: boolean;

  /** The description for templates (environment variables, ports, storages, etc.) */
  @Input()
  description: Object;

  /** Subject for debouncing the input of the image field for triggering the image analysis */
  imageAnalysisSearch: Subject<any> = new Subject<any>();

  constructor(private requestService: RequestService, private _bottomSheet: MatBottomSheet) { }

  /**
   * Sets the action for the imageAnalysis Subjet for triggering the image analysis
   */
  ngOnInit() {
    this.imageAnalysisSearch.pipe(debounceTime(1000), distinctUntilChanged((prev, curr) => prev.image === curr.image)).subscribe(e => {
      this.setImageAnalysis(e.image, e.container);
    });
  }

  /**
   * Adds a new container to the request
   */
  addContainer(): void {
    var container = new Container();
    container.containerAdvancedOptions.gracePeriod = null;
    this.request.containers.push(container);
  }

  /**
   * Deletes a container from the request and deletes all corresponding links
   * 
   * @param container The container which should be deleted
   */
  deleteContainer(container: Container): void {
    this.deleteLinks(container);
    this.request.containers = this.request.containers.filter(c => c !== container);
  }

  /**
   * Deletes all links which start at a given or end at a given container
   * 
   * @param container 
   */
  deleteLinks(container: Container) {
    var links = [];

    for (let link of this.request.links) {
      if (link.containerFrom !== container.uniqueName && link.containerTo !== container.uniqueName) {
        links.push(link);
      }
    }

    this.request.links = links;
  }

  /**
   * Searches the Description for a given container
   * 
   * @param name The name of the container (not the unique name)
   * @returns An object containing the container description or null if no description for the given name was found
   */
  getDescriptionForContainer(name: string): Object {
    if (this.description === null || this.description === undefined || name === null || name === undefined) {
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
   * Returns the actual description of an object from a JSON object
   * 
   * @param obj The JSON object from which the description should get extracted
   * @param name The name of the object for which a description should get extracted
   * @returns An object containing the description or null if no description for the given name was found
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
   * Changes the Name of the global description object of a container
   * 
   * @param name The new name of the container
   * @param description The description of the container
   */
  changeDescriptionName(name: string, description: Object) {
    if (description === null || description === undefined) {
      return;
    }

    description['name'] = name;
  }

  /**
   * Puts a new term in the imageAnalysis subject for getting a new image analysis
   * 
   * @param image The name of the image
   * @param container The container which needs the image analysis
   */
  getImageAnalysis(image: string, container: Container) {
    this.imageAnalysisSearch.next({ image: image, container: container });
  }

  /**
   * Starts the process of getting an image analysis for an image
   * 
   * @param image The name of the requested image
   * @param container The container which needs the image analysis
   */
  setImageAnalysis(image: string, container: Container) {
    if (image === undefined || image === null || image.trim() === '') {
      this.setContainerAnalysis(container, null);
      return;
    }

    container.analysis = null;
    container.loadingAnalysis = true;
    this.requestService.getImageAnalysis(image).subscribe(
      v => {
        this.setContainerAnalysis(container, v);
      },
      () => {
        this.errorImageAnalysis(container, image);
      }
    );
  }

  /**
   * Sets the image analysis for a given container if the received analysis is for the currently chosen image
   * 
   * @param container The container which requested an image analysis
   * @param analysis An analysis for an image
   */
  setContainerAnalysis(container: Container, analysis: ImageAnalysis) {
    if ((container.image === undefined || container.image === null || container.image.trim() === '') && analysis === null) {
      container.analysis = null;
      container.loadingAnalysis = false;
      return;
    }

    if (this.compareAnalysisNameWithImageName(container.image, analysis.name)) {
      container.analysis = analysis;
      container.loadingAnalysis = false;
    }
  }

  /**
   * Compares the image of an container and the name of an image analysis. In case the imageName got no tag it appends :latest to the imageName
   * 
   * @param imageName The name of the container image
   * @param analysisImage The name of the image analysis
   * @returns true, if the imagename equals the name of the analysis and false otherwise
   */
  compareAnalysisNameWithImageName(imageName: string, analysisImage: string): boolean {
    var checkName;

    if (imageName.indexOf(':') === -1) {
      checkName = `${imageName}:latest`;
    } else {
      checkName = imageName;
    }

    if (checkName === analysisImage) {
      return true;
    }

    return false;
  }

  /**
   * Stops the loading animation in the frontend in case fetching the image analysis fails
   * 
   * @param container The container which needs the image analysis
   * @param image The name of the image
   */
  errorImageAnalysis(container: Container, image: string) {
    console.log(`Could not analyze image ${image}`);
    if (container.image === null || container.image === undefined || container.image.trim() === '') {
      container.loadingAnalysis = false;
    }

    if (container.image === image) {
      container.loadingAnalysis = false;
    }
  }

  /**
   * Appends all ports of the image analysis to the containers ports
   * 
   * @param container The container for which all image analysis ports should get appended
   */
  appendImagePorts(container: Container) {
    if (container.analysis === null || container.analysis.ports === undefined || container.analysis.ports === null || container.analysis.ports.length === 0) {
      return;
    }

    for (let analysisPort of container.analysis.ports) {
      var insert = true;

      for (var i = 0; i < container.ports.length && insert; i++) {
        if (container.ports[i].port === analysisPort.port) {
          insert = false;
        }
      }

      if (insert) {
        var port = new Port();
        port.port = analysisPort.port;
        port.protocol = analysisPort.protocol;
        container.ports.push(port);
      }
    }
  }

  /**
   * Appends all environment variables of the image analysis to the container environment variables
   * 
   * @param container The container for which all analysis environment variables should get appended
   */
  appendImageVariables(container: Container) {
    if (container.analysis === null || container.analysis.environments === undefined || container.analysis.environments === null || container.analysis.environments.length === 0) {
      return;
    }

    for (let analysisVariable of container.analysis.environments) {
      var insert = true;

      for (var i = 0; i < container.environments.length && insert; i++) {
        if (container.environments[i].name === analysisVariable.name) {
          insert = false;
        }
      }

      if (insert) {
        var variable = new EnvironmentVariable();
        variable.name = analysisVariable.name;
        variable.value = analysisVariable.value;

        container.environments.push(variable);
      }
    }
  }

  /**
   * Appends all storage of the image analysis 
   * 
   * @param container The container for which all image analysis storages should get appended
   */
  appendImageMountPaths(container: Container) {
    if (container.analysis === null || container.analysis.mountPaths === undefined || container.analysis.mountPaths === null || container.analysis.mountPaths.length === 0) {
      return;
    }

    for (let analysisMountPath of container.analysis.mountPaths) {
      var insert = true;
      for (let i = 0; i < container.storage.length && insert; i++) {
        var storage = container.storage[i];

        for (let j = 0; j < storage.storageMounts.length && insert; j++) {
          if (storage.storageMounts[j].mountPath === analysisMountPath) {
            insert = false;
          }
        }
      }

      if (insert) {
        var storage = new Storage();
        storage.storageMounts = [];

        var storageMount = new StorageMount();
        storageMount.mountPath = analysisMountPath;
        storage.storageMounts.push(storageMount);

        container.storage.push(storage);
      }
    }
  }

  /**
   * Searches the Docker Hub Registry for images
   */
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
    );
    
    openBottomSheet(Sheet : any): void {
      this._bottomSheet.open(Sheet);
    }
    

}
