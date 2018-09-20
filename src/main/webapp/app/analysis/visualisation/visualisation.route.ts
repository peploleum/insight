import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { VisualisationComponent } from './visualisation.component';
import { UserRouteAccessService } from 'app/core';

export const visualisationRoute: Routes = [
    {
        path: 'visualisation',
        component: VisualisationComponent,
        canActivate: [UserRouteAccessService]
    }
];
