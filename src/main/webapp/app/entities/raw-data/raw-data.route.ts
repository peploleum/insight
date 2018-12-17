import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RawData } from 'app/shared/model/raw-data.model';
import { RawDataService } from './raw-data.service';
import { RawDataComponent } from './raw-data.component';
import { RawDataDetailComponent } from './raw-data-detail.component';
import { RawDataUpdateComponent } from './raw-data-update.component';
import { RawDataDeletePopupComponent } from './raw-data-delete-dialog.component';
import { IRawData } from 'app/shared/model/raw-data.model';

@Injectable({ providedIn: 'root' })
export class RawDataResolve implements Resolve<IRawData> {
    constructor(private service: RawDataService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<RawData> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<RawData>) => response.ok),
                map((rawData: HttpResponse<RawData>) => rawData.body)
            );
        }
        return of(new RawData());
    }
}

export const rawDataRoute: Routes = [
    {
        path: 'raw-data',
        component: RawDataComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.rawData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'raw-data/:id/view',
        component: RawDataDetailComponent,
        resolve: {
            rawData: RawDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.rawData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'raw-data/new',
        component: RawDataUpdateComponent,
        resolve: {
            rawData: RawDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.rawData.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'raw-data/:id/edit',
        component: RawDataUpdateComponent,
        resolve: {
            rawData: RawDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.rawData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rawDataPopupRoute: Routes = [
    {
        path: 'raw-data/:id/delete',
        component: RawDataDeletePopupComponent,
        resolve: {
            rawData: RawDataResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.rawData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
