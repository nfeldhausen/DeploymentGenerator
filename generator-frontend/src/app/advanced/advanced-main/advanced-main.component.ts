import { Component, OnInit } from '@angular/core';
import { Request } from '../../model/Request';
import { RequestService } from '../../services/request.service';
import { DatabaseConfiguration } from '../../model/database/DatabaseConfiguration';
import { Router, NavigationEnd } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ErrorModalComponent } from '../../error-modal/error-modal.component';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from 'src/app/animations/animations';


@Component({
  selector: 'app-advanced-main',
  templateUrl: './advanced-main.component.html',
  styleUrls: ['./advanced-main.component.css'],
  animations: [
    trigger(
      "blockInitialRenderAnimation",
      [
        transition(":enter", [])
      ]
    ),
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
 * Class which is renders the main form of the advanced mode
 */
export class AdvancedMainComponent implements OnInit {
  /** Array containing all available databases of the backend */
  availableDatabases: DatabaseConfiguration[];

  /** Request which get sent to the backend */
  request: Request = new Request();

  /** Wheter the form got already submitted or not */
  submitted = false;

  /** Switches to result mode if true */
  showResult = false;

  /** The Kubernetes deployment YAML */
  deployment: string;

  /** The description for templates */
  description: Object;

  /** Navigation subscription to evaluate calls from result mode */
  navigationSubscription;

  constructor(private requestService: RequestService, private router: Router, private modalService: NgbModal) { 
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.goBack();
      }
    });
    if (this.router.getCurrentNavigation() && this.router.getCurrentNavigation().extras.state && this.router.getCurrentNavigation().extras.state.request) {
      this.request = this.router.getCurrentNavigation().extras.state.request;
      this.description = this.router.getCurrentNavigation().extras.state.description;
    }
  }

  /**
   * Initializes components with all available databases from the backend
   */
  ngOnInit() {
    this.getDatabases();
  }

  /**
   * Gets all available databases from the backend and repeats the action, if the fetching the databases from the backend fails
   */
  getDatabases(){
    this.requestService.getAvailableDatabases().subscribe(c => this.availableDatabases = c,
      err => {
        console.log("Failed to fetch databases. Trying again in 5 Seconds");
        setTimeout(() => this.getDatabases(), 5000);
      });
  }

  /**
   * Submits the request to the backend, if services got a port or http option or tcp/udp action set
   */
  submit(): void {
    for (let service of this.request.services) {
      if (service.normalServiceOptions && !service.normalServiceOptions.ports.length) {
        this.openModalWithMessage(`Can not create service ${service.name} without ports!`);
        return;
      }

      if (service.ingressOptions && !service.ingressOptions.httpOptions.length && !service.ingressOptions.tcpUdpOptions.length) {
        this.openModalWithMessage(`Can not create ingress service ${service.name} without any http options or udp options!`);
        return;
      }
    }

    this.requestService.getDeploymentFile(this.request).subscribe(result => this.deployment = result,err => this.openModal(err),() => this.showResultDiv());
  }

  /**
   * Shows the result div
   */
  showResultDiv(): void {
    window.scroll(0,0);
    this.showResult = true;
  }

  /**
   * Shows the advanced mode instead of the result mode again
   */
  goBack(): void {
    this.showResult = false;
    this.submitted = false;
  }

  /**
   * Resets the form and creates a new request object
   */
  reset(): void {
    this.submitted = false;
    this.request = new Request();
    this.description = null;
  }

  /**
   * Opens the error modal with a specific message
   * 
   * @param message The message which should be displayed in the error modal
   */
  openModalWithMessage(message: string): void {
    const modalRef = this.modalService.open(ErrorModalComponent);
    modalRef.componentInstance.message = message;
  }

  /**
   * Opens the error modal, when an error occurs during sending the request
   * 
   * @param error The error while sending the request
   */
  openModal(error: Object): void {
    if (typeof error['error'] === 'string') {
      var errObject = JSON.parse(error['error']);
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.message = errObject['description'];
    } else {
      const modalRef = this.modalService.open(ErrorModalComponent);
      modalRef.componentInstance.message = error['statusText'];
    }
  }
}
