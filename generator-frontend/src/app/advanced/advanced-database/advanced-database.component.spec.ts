import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedDatabaseComponent } from './advanced-database.component';

describe('AdvancedDatabaseComponent', () => {
  let component: AdvancedDatabaseComponent;
  let fixture: ComponentFixture<AdvancedDatabaseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvancedDatabaseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvancedDatabaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
