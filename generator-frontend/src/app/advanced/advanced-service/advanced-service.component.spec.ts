import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedServiceComponent } from './advanced-service.component';

describe('AdvancedServiceComponent', () => {
  let component: AdvancedServiceComponent;
  let fixture: ComponentFixture<AdvancedServiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvancedServiceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvancedServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
