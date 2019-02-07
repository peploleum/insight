import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardManagerComponent } from 'app/dashboard/side/dashboard-manager.component';

const routes: Routes = [
    {
        path: 'dashboard',
        component: DashboardManagerComponent,
        data: {
            pageTitle: 'sources.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DashboardRoutingModule {}
