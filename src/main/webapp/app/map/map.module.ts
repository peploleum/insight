import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { EventThreadComponent } from './event-thread.component';
import { InsightSharedModule } from 'app/shared';
import { MapMenuComponent } from './map-menu.component';

@NgModule({
    imports: [CommonModule, MapRoutingModule, InsightSharedModule],
    declarations: [MapComponent, EventThreadComponent, MapMenuComponent]
})
export class MapModule {}
