/**
 * Created by gFolgoas on 07/02/2019.
 */
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SideComponent } from './side.component';

const routes: Routes = [
    {
        path: 'ins-map-side-panel',
        component: SideComponent,
        data: {
            target: 'map'
        },
        outlet: 'side'
    },
    {
        path: 'ins-dashboard-side-panel',
        component: SideComponent,
        data: {
            target: 'dashboard'
        },
        outlet: 'side'
    },
    {
        path: 'ins-network-side-panel',
        component: SideComponent,
        data: {
            target: 'network'
        },
        outlet: 'side'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SideRoutingModule {}
