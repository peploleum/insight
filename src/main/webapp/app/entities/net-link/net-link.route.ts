import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { NetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from './net-link.service';
import { NetLinkComponent } from './net-link.component';
import { NetLinkDetailComponent } from './net-link-detail.component';
import { NetLinkUpdateComponent } from './net-link-update.component';
import { NetLinkDeletePopupComponent } from './net-link-delete-dialog.component';
import { INetLink } from 'app/shared/model/net-link.model';

@Injectable({ providedIn: 'root' })
export class NetLinkResolve implements Resolve<INetLink> {
    constructor(private service: NetLinkService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((netLink: HttpResponse<NetLink>) => netLink.body));
        }
        return of(new NetLink());
    }
}

export const netLinkRoute: Routes = [
    {
        path: 'net-link',
        component: NetLinkComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.netLink.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'net-link/:id/view',
        component: NetLinkDetailComponent,
        resolve: {
            netLink: NetLinkResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.netLink.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'net-link/new',
        component: NetLinkUpdateComponent,
        resolve: {
            netLink: NetLinkResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.netLink.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'net-link/:id/edit',
        component: NetLinkUpdateComponent,
        resolve: {
            netLink: NetLinkResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.netLink.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const netLinkPopupRoute: Routes = [
    {
        path: 'net-link/:id/delete',
        component: NetLinkDeletePopupComponent,
        resolve: {
            netLink: NetLinkResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.netLink.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
