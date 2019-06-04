import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MapRoutingModule } from './map-routing.module';
import { MapComponent } from './map.component';
import { InsightSharedModule } from 'app/shared';
import { ReactiveFormsModule } from '@angular/forms';
import { ColorPickerModule } from 'ngx-color-picker';
import { DessinComponent } from './side/dessin.component';
import { QuickStatsComponent } from './side/quick-stats.component';
import { LayerControlComponent } from './side/layer-control.component';
import { MapSearchComponent } from './side/map-search.component';
import { MapOverlayComponent } from './map-overlay.component';
import { ContentFilterComponent } from './side/content-filter.component';

@NgModule({
    imports: [CommonModule, MapRoutingModule, InsightSharedModule, ReactiveFormsModule, ColorPickerModule],
    declarations: [
        MapComponent,
        DessinComponent,
        QuickStatsComponent,
        LayerControlComponent,
        MapSearchComponent,
        MapOverlayComponent,
        ContentFilterComponent
    ],
    entryComponents: [MapOverlayComponent],
    exports: [DessinComponent, QuickStatsComponent, LayerControlComponent, MapSearchComponent, ContentFilterComponent]
})
export class MapModule {}
