import { enterHeightAnimation } from './../../animations/animations';
import { trigger, useAnimation, transition } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { MainWizardOptions } from '../../model/wizard/MainWizardOptions';
import { Request } from '../../model/Request';

@Component({
  selector: 'app-wizard-stepper-main',
  templateUrl: './wizard-stepper-main.component.html',
  styleUrls: ['./wizard-stepper-main.component.css'],
  animations: [
    trigger(
      'enterAnimation', [
      transition(':enter', [
        useAnimation(enterHeightAnimation, {
          params: {
            startHeight: '0',
            endHeight: '*',
            time: '1000'
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
    )
  ]
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
