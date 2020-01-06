import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedLinkComponent } from './advanced-link.component';

describe('AdvancedLinkComponent', () => {
  let component: AdvancedLinkComponent;
  let fixture: ComponentFixture<AdvancedLinkComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvancedLinkComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvancedLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
