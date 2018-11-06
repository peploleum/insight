import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    IntrusionSetComponent,
    IntrusionSetDetailComponent,
    IntrusionSetUpdateComponent,
    IntrusionSetDeletePopupComponent,
    IntrusionSetDeleteDialogComponent,
    intrusionSetRoute,
    intrusionSetPopupRoute
} from './';

const ENTITY_STATES = [...intrusionSetRoute, ...intrusionSetPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        IntrusionSetComponent,
        IntrusionSetDetailComponent,
        IntrusionSetUpdateComponent,
        IntrusionSetDeleteDialogComponent,
        IntrusionSetDeletePopupComponent
    ],
    entryComponents: [
        IntrusionSetComponent,
        IntrusionSetUpdateComponent,
        IntrusionSetDeleteDialogComponent,
        IntrusionSetDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightIntrusionSetModule {}
