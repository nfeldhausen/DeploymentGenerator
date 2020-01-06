import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardStepperContainerComponent } from './wizard-stepper-container.component';

describe('WizardStepperContainerComponent', () => {
  let component: WizardStepperContainerComponent;
  let fixture: ComponentFixture<WizardStepperContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardStepperContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardStepperContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
