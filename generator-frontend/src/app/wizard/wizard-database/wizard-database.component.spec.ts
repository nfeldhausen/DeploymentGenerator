import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WizardDatabaseComponent } from './wizard-database.component';

describe('WizardDatabaseComponent', () => {
  let component: WizardDatabaseComponent;
  let fixture: ComponentFixture<WizardDatabaseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WizardDatabaseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WizardDatabaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
