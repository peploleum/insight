import { Routes } from '@angular/router';
import { AnalyticsComponent } from './analytics.component';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const analyticsRoute: Routes = [
    {
        path: 'analytics',
        component: AnalyticsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.analytics.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
