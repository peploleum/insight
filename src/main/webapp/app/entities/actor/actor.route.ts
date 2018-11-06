import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Actor } from 'app/shared/model/actor.model';
import { ActorService } from './actor.service';
import { ActorComponent } from './actor.component';
import { ActorDetailComponent } from './actor-detail.component';
import { ActorUpdateComponent } from './actor-update.component';
import { ActorDeletePopupComponent } from './actor-delete-dialog.component';
import { IActor } from 'app/shared/model/actor.model';

@Injectable({ providedIn: 'root' })
export class ActorResolve implements Resolve<IActor> {
    constructor(private service: ActorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((actor: HttpResponse<Actor>) => actor.body));
        }
        return of(new Actor());
    }
}

export const actorRoute: Routes = [
    {
        path: 'actor',
        component: ActorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'actor/:id/view',
        component: ActorDetailComponent,
        resolve: {
            actor: ActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'actor/new',
        component: ActorUpdateComponent,
        resolve: {
            actor: ActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'actor/:id/edit',
        component: ActorUpdateComponent,
        resolve: {
            actor: ActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const actorPopupRoute: Routes = [
    {
        path: 'actor/:id/delete',
        component: ActorDeletePopupComponent,
        resolve: {
            actor: ActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.actor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
