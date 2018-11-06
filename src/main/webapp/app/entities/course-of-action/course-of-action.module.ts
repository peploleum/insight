import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    CourseOfActionComponent,
    CourseOfActionDetailComponent,
    CourseOfActionUpdateComponent,
    CourseOfActionDeletePopupComponent,
    CourseOfActionDeleteDialogComponent,
    courseOfActionRoute,
    courseOfActionPopupRoute
} from './';

const ENTITY_STATES = [...courseOfActionRoute, ...courseOfActionPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CourseOfActionComponent,
        CourseOfActionDetailComponent,
        CourseOfActionUpdateComponent,
        CourseOfActionDeleteDialogComponent,
        CourseOfActionDeletePopupComponent
    ],
    entryComponents: [
        CourseOfActionComponent,
        CourseOfActionUpdateComponent,
        CourseOfActionDeleteDialogComponent,
        CourseOfActionDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightCourseOfActionModule {}
