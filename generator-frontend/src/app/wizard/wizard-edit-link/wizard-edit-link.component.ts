import { Component, OnInit, Input } from '@angular/core';
import { Request } from 'src/app/model/Request';
import { MainWizardOptions } from 'src/app/model/wizard/MainWizardOptions';
import { BaseContainer } from 'src/app/model/BaseContainer';
import { Link } from 'src/app/model/Link';

@Component({
  selector: 'app-wizard-edit-link',
  templateUrl: './wizard-edit-link.component.html',
  styleUrls: ['./wizard-edit-link.component.css']
})
/** Class which is necessary for displaying the wizard edit link page */
export class WizardEditLinkComponent implements OnInit {
  /** Request which gets created */
  @Input()
  request: Request;

  /** Global wizard options */
  @Input()
  mainWizardOptions: MainWizardOptions;

  /** Link which is being modified */
  @Input()
  link: Link = null;

  /** Whether the form was already submitted or not */
  submitted: boolean = false;

  /** Submit Type for getting next page */
  submitType: string;

  constructor() { }

  /**
   * Creates a new link if no link was given
   */
  ngOnInit() {
    if (this.link === null) {
      this.link = new Link();
    }
  }

  /**
   * Gets all containers and databases of the request
   */
  getAllContainer(): BaseContainer[] {
    return (<BaseContainer[]>this.request.containers).concat(<BaseContainer[]>(this.request.databases));
  }

  /**
   * Gets next page of the wizard
   */
  getNextPage() {
    if (this.submitType === 'INSERT') {
      this.request.links.push(this.link);
    }
    
    this.mainWizardOptions.page = 'RESULT';
  }
}