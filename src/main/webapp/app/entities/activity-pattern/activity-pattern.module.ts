import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    ActivityPatternComponent,
    ActivityPatternDetailComponent,
    ActivityPatternUpdateComponent,
    ActivityPatternDeletePopupComponent,
    ActivityPatternDeleteDialogComponent,
    activityPatternRoute,
    activityPatternPopupRoute
} from './';

const ENTITY_STATES = [...activityPatternRoute, ...activityPatternPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ActivityPatternComponent,
        ActivityPatternDetailComponent,
        ActivityPatternUpdateComponent,
        ActivityPatternDeleteDialogComponent,
        ActivityPatternDeletePopupComponent
    ],
    entryComponents: [
        ActivityPatternComponent,
        ActivityPatternUpdateComponent,
        ActivityPatternDeleteDialogComponent,
        ActivityPatternDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightActivityPatternModule {}
