import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardStepperMainComponent } from './wizard-stepper-main';

describe('WizardStepperMainComponent', () => {
  let component: WizardStepperMainComponent;
  let fixture: ComponentFixture<WizardStepperMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardStepperMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardStepperMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
