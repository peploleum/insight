import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Equipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from './equipment.service';
import { EquipmentComponent } from './equipment.component';
import { EquipmentDetailComponent } from './equipment-detail.component';
import { EquipmentUpdateComponent } from './equipment-update.component';
import { EquipmentDeletePopupComponent } from './equipment-delete-dialog.component';
import { IEquipment } from 'app/shared/model/equipment.model';

@Injectable({ providedIn: 'root' })
export class EquipmentResolve implements Resolve<IEquipment> {
    constructor(private service: EquipmentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Equipment> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Equipment>) => response.ok),
                map((equipment: HttpResponse<Equipment>) => equipment.body)
            );
        }
        return of(new Equipment());
    }
}

export const equipmentRoute: Routes = [
    {
        path: 'equipment',
        component: EquipmentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.equipment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipment/:id/view',
        component: EquipmentDetailComponent,
        resolve: {
            equipment: EquipmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.equipment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipment/new',
        component: EquipmentUpdateComponent,
        resolve: {
            equipment: EquipmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.equipment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'equipment/:id/edit',
        component: EquipmentUpdateComponent,
        resolve: {
            equipment: EquipmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.equipment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const equipmentPopupRoute: Routes = [
    {
        path: 'equipment/:id/delete',
        component: EquipmentDeletePopupComponent,
        resolve: {
            equipment: EquipmentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.equipment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
