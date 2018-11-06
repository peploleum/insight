import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivityPattern } from 'app/shared/model/activity-pattern.model';
import { ActivityPatternService } from './activity-pattern.service';
import { ActivityPatternComponent } from './activity-pattern.component';
import { ActivityPatternDetailComponent } from './activity-pattern-detail.component';
import { ActivityPatternUpdateComponent } from './activity-pattern-update.component';
import { ActivityPatternDeletePopupComponent } from './activity-pattern-delete-dialog.component';
import { IActivityPattern } from 'app/shared/model/activity-pattern.model';

@Injectable({ providedIn: 'root' })
export class ActivityPatternResolve implements Resolve<IActivityPattern> {
    constructor(private service: ActivityPatternService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((activityPattern: HttpResponse<ActivityPattern>) => activityPattern.body));
        }
        return of(new ActivityPattern());
    }
}

export const activityPatternRoute: Routes = [
    {
        path: 'activity-pattern',
        component: ActivityPatternComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.activityPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'activity-pattern/:id/view',
        component: ActivityPatternDetailComponent,
        resolve: {
            activityPattern: ActivityPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.activityPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'activity-pattern/new',
        component: ActivityPatternUpdateComponent,
        resolve: {
            activityPattern: ActivityPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.activityPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'activity-pattern/:id/edit',
        component: ActivityPatternUpdateComponent,
        resolve: {
            activityPattern: ActivityPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.activityPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const activityPatternPopupRoute: Routes = [
    {
        path: 'activity-pattern/:id/delete',
        component: ActivityPatternDeletePopupComponent,
        resolve: {
            activityPattern: ActivityPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.activityPattern.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
