import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Tool } from 'app/shared/model/tool.model';
import { ToolService } from './tool.service';
import { ToolComponent } from './tool.component';
import { ToolDetailComponent } from './tool-detail.component';
import { ToolUpdateComponent } from './tool-update.component';
import { ToolDeletePopupComponent } from './tool-delete-dialog.component';
import { ITool } from 'app/shared/model/tool.model';

@Injectable({ providedIn: 'root' })
export class ToolResolve implements Resolve<ITool> {
    constructor(private service: ToolService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((tool: HttpResponse<Tool>) => tool.body));
        }
        return of(new Tool());
    }
}

export const toolRoute: Routes = [
    {
        path: 'tool',
        component: ToolComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.tool.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tool/:id/view',
        component: ToolDetailComponent,
        resolve: {
            tool: ToolResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.tool.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tool/new',
        component: ToolUpdateComponent,
        resolve: {
            tool: ToolResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.tool.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'tool/:id/edit',
        component: ToolUpdateComponent,
        resolve: {
            tool: ToolResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.tool.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const toolPopupRoute: Routes = [
    {
        path: 'tool/:id/delete',
        component: ToolDeletePopupComponent,
        resolve: {
            tool: ToolResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.tool.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
