import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Organisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from './organisation.service';
import { OrganisationComponent } from './organisation.component';
import { OrganisationDetailComponent } from './organisation-detail.component';
import { OrganisationUpdateComponent } from './organisation-update.component';
import { OrganisationDeletePopupComponent } from './organisation-delete-dialog.component';
import { IOrganisation } from 'app/shared/model/organisation.model';

@Injectable({ providedIn: 'root' })
export class OrganisationResolve implements Resolve<IOrganisation> {
    constructor(private service: OrganisationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Organisation> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Organisation>) => response.ok),
                map((organisation: HttpResponse<Organisation>) => organisation.body)
            );
        }
        return of(new Organisation());
    }
}

export const organisationRoute: Routes = [
    {
        path: 'organisation',
        component: OrganisationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.organisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'organisation/:id/view',
        component: OrganisationDetailComponent,
        resolve: {
            organisation: OrganisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.organisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'organisation/new',
        component: OrganisationUpdateComponent,
        resolve: {
            organisation: OrganisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.organisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'organisation/:id/edit',
        component: OrganisationUpdateComponent,
        resolve: {
            organisation: OrganisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.organisation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const organisationPopupRoute: Routes = [
    {
        path: 'organisation/:id/delete',
        component: OrganisationDeletePopupComponent,
        resolve: {
            organisation: OrganisationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.organisation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
