import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NetworkComponent } from 'app/network/network.component';

const routes: Routes = [
    {
        path: 'network',
        component: NetworkComponent,
        data: {
            pageTitle: 'network.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class NetworkRoutingModule {}
