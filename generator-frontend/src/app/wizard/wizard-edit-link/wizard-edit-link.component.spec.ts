import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardEditLinkComponent } from './wizard-edit-link.component';

describe('WizardEditLinkComponent', () => {
  let component: WizardEditLinkComponent;
  let fixture: ComponentFixture<WizardEditLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardEditLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardEditLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
