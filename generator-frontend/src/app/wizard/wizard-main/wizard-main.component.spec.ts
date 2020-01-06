import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardMainComponent } from './wizard-main.component';

describe('WizardMainComponent', () => {
  let component: WizardMainComponent;
  let fixture: ComponentFixture<WizardMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
