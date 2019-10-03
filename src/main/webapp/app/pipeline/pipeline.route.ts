import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { PipelineComponent } from 'app/pipeline/pipeline.component';

export const pipelineRoute: Routes = [
    {
        path: 'pipeline',
        component: PipelineComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.pipeline.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
