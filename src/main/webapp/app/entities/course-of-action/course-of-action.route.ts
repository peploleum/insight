import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CourseOfAction } from 'app/shared/model/course-of-action.model';
import { CourseOfActionService } from './course-of-action.service';
import { CourseOfActionComponent } from './course-of-action.component';
import { CourseOfActionDetailComponent } from './course-of-action-detail.component';
import { CourseOfActionUpdateComponent } from './course-of-action-update.component';
import { CourseOfActionDeletePopupComponent } from './course-of-action-delete-dialog.component';
import { ICourseOfAction } from 'app/shared/model/course-of-action.model';

@Injectable({ providedIn: 'root' })
export class CourseOfActionResolve implements Resolve<ICourseOfAction> {
    constructor(private service: CourseOfActionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((courseOfAction: HttpResponse<CourseOfAction>) => courseOfAction.body));
        }
        return of(new CourseOfAction());
    }
}

export const courseOfActionRoute: Routes = [
    {
        path: 'course-of-action',
        component: CourseOfActionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'insightApp.courseOfAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-of-action/:id/view',
        component: CourseOfActionDetailComponent,
        resolve: {
            courseOfAction: CourseOfActionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.courseOfAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-of-action/new',
        component: CourseOfActionUpdateComponent,
        resolve: {
            courseOfAction: CourseOfActionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.courseOfAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-of-action/:id/edit',
        component: CourseOfActionUpdateComponent,
        resolve: {
            courseOfAction: CourseOfActionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.courseOfAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courseOfActionPopupRoute: Routes = [
    {
        path: 'course-of-action/:id/delete',
        component: CourseOfActionDeletePopupComponent,
        resolve: {
            courseOfAction: CourseOfActionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.courseOfAction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
