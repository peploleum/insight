import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { EventThreadComponent } from './side/event-thread.component';
import { InsightSharedModule } from 'app/shared';
import { MapMenuComponent } from './map-menu.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SideComponent } from './side/side.component';
import { ColorPickerModule } from 'ngx-color-picker';
import { DessinComponent } from './side/dessin.component';

@NgModule({
    imports: [CommonModule, MapRoutingModule, InsightSharedModule, ReactiveFormsModule, ColorPickerModule],
    declarations: [MapComponent, SideComponent, EventThreadComponent, DessinComponent, MapMenuComponent]
})
export class MapModule {}
