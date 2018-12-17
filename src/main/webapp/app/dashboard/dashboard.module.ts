import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DashboardRoutingModule} from './dashboard-routing.module';
import {DashboardManagerComponent} from './dashboard-manager.component';
import {DashboardCreateComponent} from './dashboard-create.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
    declarations: [DashboardManagerComponent, DashboardCreateComponent],
    imports: [CommonModule, DashboardRoutingModule, ReactiveFormsModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DashboardModule {
}
