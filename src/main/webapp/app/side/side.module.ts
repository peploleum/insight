/**
 * Created by gFolgoas on 07/02/2019.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InsightSharedModule } from 'app/shared';
import { SideComponent } from './side.component';
import { SideRoutingModule } from './side-routing.module';
import { MapModule } from '../map/map.module';
import { DashboardModule } from '../dashboard/dashboard.module';
import { SideEventThreadComponent } from './side-event-thread.component';

@NgModule({
    imports: [CommonModule, InsightSharedModule, SideRoutingModule, MapModule, DashboardModule],
    declarations: [SideComponent, SideEventThreadComponent]
})
export class SideModule {}
