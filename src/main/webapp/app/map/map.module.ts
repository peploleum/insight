import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MapRoutingModule} from './map-routing.module';
import {MapComponent} from './map.component';
import {EventThreadComponent} from './event-thread.component';
import {InsightSharedModule} from 'app/shared';

@NgModule({
    imports: [CommonModule, MapRoutingModule, InsightSharedModule],
    declarations: [MapComponent, EventThreadComponent]
})
export class MapModule {
}
