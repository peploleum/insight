import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardContainerComponent } from './dashboard-container.component';

const routes: Routes = [
    {
        path: 'dashboard',
        component: DashboardContainerComponent,
        data: {
            pageTitle: 'dashboard.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DashboardRoutingModule {}
