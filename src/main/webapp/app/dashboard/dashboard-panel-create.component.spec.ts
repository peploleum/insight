import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPanelCreateComponent } from './dashboard-panel-create.component';

describe('DashboardPanelCreateComponent', () => {
  let component: DashboardPanelCreateComponent;
  let fixture: ComponentFixture<DashboardPanelCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardPanelCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardPanelCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
