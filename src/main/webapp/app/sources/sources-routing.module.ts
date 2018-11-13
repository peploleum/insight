import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SourcesManagerComponent} from 'app/sources/sources-manager.component';

const routes: Routes = [{
    path: 'sources',
    component: SourcesManagerComponent,
    data: {
        pageTitle: 'sources.title'
    }
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SourcesRoutingModule {
}
