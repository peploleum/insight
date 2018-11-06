import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { IntrusionSet } from 'app/shared/model/intrusion-set.model';
import { IntrusionSetService } from './intrusion-set.service';
import { IntrusionSetComponent } from './intrusion-set.component';
import { IntrusionSetDetailComponent } from './intrusion-set-detail.component';
import { IntrusionSetUpdateComponent } from './intrusion-set-update.component';
import { IntrusionSetDeletePopupComponent } from './intrusion-set-delete-dialog.component';
import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';

@Injectable({ providedIn: 'root' })
export class IntrusionSetResolve implements Resolve<IIntrusionSet> {
    constructor(private service: IntrusionSetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((intrusionSet: HttpResponse<IntrusionSet>) => intrusionSet.body));
        }
        return of(new IntrusionSet());
    }
}

export const intrusionSetRoute: Routes = [
    {
        path: 'intrusion-set',
        component: IntrusionSetComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.intrusionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'intrusion-set/:id/view',
        component: IntrusionSetDetailComponent,
        resolve: {
            intrusionSet: IntrusionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.intrusionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'intrusion-set/new',
        component: IntrusionSetUpdateComponent,
        resolve: {
            intrusionSet: IntrusionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.intrusionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'intrusion-set/:id/edit',
        component: IntrusionSetUpdateComponent,
        resolve: {
            intrusionSet: IntrusionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.intrusionSet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const intrusionSetPopupRoute: Routes = [
    {
        path: 'intrusion-set/:id/delete',
        component: IntrusionSetDeletePopupComponent,
        resolve: {
            intrusionSet: IntrusionSetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.intrusionSet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
