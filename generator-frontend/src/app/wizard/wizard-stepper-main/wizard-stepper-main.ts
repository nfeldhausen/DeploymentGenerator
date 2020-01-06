import { Component, OnInit } from '@angular/core';
import { MainWizardOptions } from '../../model/wizard/MainWizardOptions';
import { Request } from '../../model/Request';

@Component({
  selector: 'app-wizard-stepper-main',
  templateUrl: './wizard-stepper-main.component.html',
  styleUrls: ['./wizard-stepper-main.component.css']
})
export class WizardStepperMainComponent implements OnInit {
  /** Global wizard options */
  mainWizardOptions: MainWizardOptions = new MainWizardOptions();

  /** Request which gets created */
  request: Request = new Request();

  constructor() { }

  ngOnInit() {
  }

}
