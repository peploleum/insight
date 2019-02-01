import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MapComponent } from './map.component';
import { EventThreadComponent } from './side/event-thread.component';
import { SideComponent } from './side/side.component';

const routes: Routes = [
    {
        path: 'map',
        component: MapComponent,
        data: {
            pageTitle: 'map.title'
        }
    },
    {
        path: 'ins-map-side-panel',
        component: SideComponent,
        outlet: 'side'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MapRoutingModule {}
