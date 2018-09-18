import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {NetworkComponent} from './network.component';

const routes: Routes = [{
    path: 'network',
    component: NetworkComponent,
    data: {
        pageTitle: 'network.title'
    }
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class NetworkRoutingModule {
}
