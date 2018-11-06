import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ThreatActor } from 'app/shared/model/threat-actor.model';
import { ThreatActorService } from './threat-actor.service';
import { ThreatActorComponent } from './threat-actor.component';
import { ThreatActorDetailComponent } from './threat-actor-detail.component';
import { ThreatActorUpdateComponent } from './threat-actor-update.component';
import { ThreatActorDeletePopupComponent } from './threat-actor-delete-dialog.component';
import { IThreatActor } from 'app/shared/model/threat-actor.model';

@Injectable({ providedIn: 'root' })
export class ThreatActorResolve implements Resolve<IThreatActor> {
    constructor(private service: ThreatActorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((threatActor: HttpResponse<ThreatActor>) => threatActor.body));
        }
        return of(new ThreatActor());
    }
}

export const threatActorRoute: Routes = [
    {
        path: 'threat-actor',
        component: ThreatActorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.threatActor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'threat-actor/:id/view',
        component: ThreatActorDetailComponent,
        resolve: {
            threatActor: ThreatActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.threatActor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'threat-actor/new',
        component: ThreatActorUpdateComponent,
        resolve: {
            threatActor: ThreatActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.threatActor.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'threat-actor/:id/edit',
        component: ThreatActorUpdateComponent,
        resolve: {
            threatActor: ThreatActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.threatActor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const threatActorPopupRoute: Routes = [
    {
        path: 'threat-actor/:id/delete',
        component: ThreatActorDeletePopupComponent,
        resolve: {
            threatActor: ThreatActorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.threatActor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
