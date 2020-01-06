import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardResultComponent } from './wizard-result.component';

describe('WizardResultComponent', () => {
  let component: WizardResultComponent;
  let fixture: ComponentFixture<WizardResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
