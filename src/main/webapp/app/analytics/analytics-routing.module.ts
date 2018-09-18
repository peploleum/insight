import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AnalyticsComponent} from './analytics.component';

const routes: Routes = [{
    path: 'analytics',
    component: AnalyticsComponent,
    data: {
        pageTitle: 'analytics.title'
    }
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AnalyticsRoutingModule {
}
