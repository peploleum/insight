import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ObservedData } from 'app/shared/model/observed-data.model';
import { ObservedDataService } from './observed-data.service';
import { ObservedDataComponent } from './observed-data.component';
import { ObservedDataDetailComponent } from './observed-data-detail.component';
import { ObservedDataUpdateComponent } from './observed-data-update.component';
import { ObservedDataDeletePopupComponent } from './observed-data-delete-dialog.component';
import { IObservedData } from 'app/shared/model/observed-data.model';

@Injectable({ providedIn: 'root' })
export class ObservedDataResolve implements Resolve<IObservedData> {
    constructor(private service: ObservedDataService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((observedData: HttpResponse<ObservedData>) => observedData.body));
        }
        return of(new ObservedData());
    }
}

export const observedDataRoute: Routes = [
    {
        path: 'observed-data',
        component: ObservedDataComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.observedData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'observed-data/:id/view',
        component: ObservedDataDetailComponent,
        resolve: {
            observedData: ObservedDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.observedData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'observed-data/new',
        component: ObservedDataUpdateComponent,
        resolve: {
            observedData: ObservedDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.observedData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'observed-data/:id/edit',
        component: ObservedDataUpdateComponent,
        resolve: {
            observedData: ObservedDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.observedData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const observedDataPopupRoute: Routes = [
    {
        path: 'observed-data/:id/delete',
        component: ObservedDataDeletePopupComponent,
        resolve: {
            observedData: ObservedDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.observedData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
