import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AttackPattern } from 'app/shared/model/attack-pattern.model';
import { AttackPatternService } from './attack-pattern.service';
import { AttackPatternComponent } from './attack-pattern.component';
import { AttackPatternDetailComponent } from './attack-pattern-detail.component';
import { AttackPatternUpdateComponent } from './attack-pattern-update.component';
import { AttackPatternDeletePopupComponent } from './attack-pattern-delete-dialog.component';
import { IAttackPattern } from 'app/shared/model/attack-pattern.model';

@Injectable({ providedIn: 'root' })
export class AttackPatternResolve implements Resolve<IAttackPattern> {
    constructor(private service: AttackPatternService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((attackPattern: HttpResponse<AttackPattern>) => attackPattern.body));
        }
        return of(new AttackPattern());
    }
}

export const attackPatternRoute: Routes = [
    {
        path: 'attack-pattern',
        component: AttackPatternComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.attackPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attack-pattern/:id/view',
        component: AttackPatternDetailComponent,
        resolve: {
            attackPattern: AttackPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.attackPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attack-pattern/new',
        component: AttackPatternUpdateComponent,
        resolve: {
            attackPattern: AttackPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.attackPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'attack-pattern/:id/edit',
        component: AttackPatternUpdateComponent,
        resolve: {
            attackPattern: AttackPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.attackPattern.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attackPatternPopupRoute: Routes = [
    {
        path: 'attack-pattern/:id/delete',
        component: AttackPatternDeletePopupComponent,
        resolve: {
            attackPattern: AttackPatternResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.attackPattern.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
