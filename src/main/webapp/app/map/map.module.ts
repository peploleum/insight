import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { EventThreadComponent } from './event-thread.component';

@NgModule({
    imports: [CommonModule, MapRoutingModule],
    declarations: [MapComponent, EventThreadComponent]
})
export class MapModule {}
