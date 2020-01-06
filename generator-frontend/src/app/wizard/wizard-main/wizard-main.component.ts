import { Component, OnInit } from '@angular/core';
import { MainWizardOptions } from 'src/app/model/wizard/MainWizardOptions';
import { Request } from 'src/app/model/Request';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-wizard-main',
  templateUrl: './wizard-main.component.html',
  styleUrls: ['./wizard-main.component.css']
})
/** Class which is necessary for displaying the wizard page */
export class WizardMainComponent implements OnInit {
  /** Global wizard options */
  mainWizardOptions: MainWizardOptions = new MainWizardOptions();

  /** Request which gets created */
  request: Request = new Request();

  constructor() { }

  ngOnInit() { }
}
