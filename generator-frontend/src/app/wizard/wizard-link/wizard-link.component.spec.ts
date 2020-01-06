import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardLinkComponent } from './wizard-link.component';

describe('WizardLinkComponent', () => {
  let component: WizardLinkComponent;
  let fixture: ComponentFixture<WizardLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
