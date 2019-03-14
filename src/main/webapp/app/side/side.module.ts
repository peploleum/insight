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
import { NetworkModule } from '../network/network.module';
import { ReactiveFormsModule } from '@angular/forms';
import { QuickViewComponent } from './quick-view.component';
import { SideButtonsComponent } from './side-buttons.component';
import { QuickAnnotationComponent } from './quick-annotation.component';

@NgModule({
    imports: [CommonModule, ReactiveFormsModule, InsightSharedModule, SideRoutingModule, MapModule, DashboardModule, NetworkModule],
    declarations: [SideComponent, SideEventThreadComponent, QuickViewComponent, SideButtonsComponent, QuickAnnotationComponent]
})
export class SideModule {}
