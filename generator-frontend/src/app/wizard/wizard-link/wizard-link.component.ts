import { Component, OnInit, Input } from '@angular/core';
import { MainWizardOptions } from 'src/app/model/wizard/MainWizardOptions';
import { Request } from 'src/app/model/Request';
import { Link } from 'src/app/model/Link';
import { BaseContainer } from 'src/app/model/BaseContainer';
import { trigger, transition, useAnimation } from '@angular/animations';
import { enterHeightAnimation } from 'src/app/animations/animations';

@Component({
  selector: 'app-wizard-link',
  templateUrl: './wizard-link.component.html',
  styleUrls: ['./wizard-link.component.css'],
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
    )
  ]
})
/** Class which is necessary for displaying the wizard link page */
export class WizardLinkComponent implements OnInit {
  /** Request which gets created */
  @Input()
  request: Request;

  /** Global wizard options */
  @Input()
  mainWizardOptions: MainWizardOptions;

  /** Whether the form was already submitted or not */
  submitted: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  /**
   * Adds a link to the request
   */
  addLink() {
    this.request.links.push(new Link());
  }

  /**
   * Deletes a link from the request
   * 
   * @param link The link which should be deleted from the request
   */
  deleteLink(link: Link) {
    this.request.links = this.request.links.filter(l => l !== link);
  }

  /**
   * Gets next page of the wizard
   */
  getNextPage() {
    this.mainWizardOptions.page = 'RESULT';
  }

  /**
   * Gets all containers and databases from the request
   */
  getAllContainer(): BaseContainer[] {
    return (<BaseContainer[]>this.request.containers).concat(<BaseContainer[]>(this.request.databases));
  }

}
