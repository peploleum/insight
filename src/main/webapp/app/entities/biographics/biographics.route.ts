import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Biographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from './biographics.service';
import { BiographicsComponent } from './biographics.component';
import { BiographicsDetailComponent } from './biographics-detail.component';
import { BiographicsUpdateComponent } from './biographics-update.component';
import { BiographicsDeletePopupComponent } from './biographics-delete-dialog.component';
import { IBiographics } from 'app/shared/model/biographics.model';

@Injectable({ providedIn: 'root' })
export class BiographicsResolve implements Resolve<IBiographics> {
    constructor(private service: BiographicsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((biographics: HttpResponse<Biographics>) => biographics.body));
        }
        return of(new Biographics());
    }
}

export const biographicsRoute: Routes = [
    {
        path: 'biographics',
        component: BiographicsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.biographics.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'biographics/:id/view',
        component: BiographicsDetailComponent,
        resolve: {
            biographics: BiographicsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.biographics.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'biographics/new',
        component: BiographicsUpdateComponent,
        resolve: {
            biographics: BiographicsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.biographics.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'biographics/:id/edit',
        component: BiographicsUpdateComponent,
        resolve: {
            biographics: BiographicsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.biographics.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const biographicsPopupRoute: Routes = [
    {
        path: 'biographics/:id/delete',
        component: BiographicsDeletePopupComponent,
        resolve: {
            biographics: BiographicsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.biographics.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
