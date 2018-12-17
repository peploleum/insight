import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardManagerComponent } from './dashboard-manager.component';

@NgModule({
    declarations: [DashboardManagerComponent],
    imports: [CommonModule, DashboardRoutingModule]
})
export class DashboardModule {}
