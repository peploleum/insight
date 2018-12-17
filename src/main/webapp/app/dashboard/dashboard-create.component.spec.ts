import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardCreateComponent } from './dashboard-create.component';

describe('DashboardCreateComponent', () => {
  let component: DashboardCreateComponent;
  let fixture: ComponentFixture<DashboardCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
