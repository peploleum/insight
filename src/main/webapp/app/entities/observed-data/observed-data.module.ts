import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    ObservedDataComponent,
    ObservedDataDetailComponent,
    ObservedDataUpdateComponent,
    ObservedDataDeletePopupComponent,
    ObservedDataDeleteDialogComponent,
    observedDataRoute,
    observedDataPopupRoute
} from './';

const ENTITY_STATES = [...observedDataRoute, ...observedDataPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ObservedDataComponent,
        ObservedDataDetailComponent,
        ObservedDataUpdateComponent,
        ObservedDataDeleteDialogComponent,
        ObservedDataDeletePopupComponent
    ],
    entryComponents: [
        ObservedDataComponent,
        ObservedDataUpdateComponent,
        ObservedDataDeleteDialogComponent,
        ObservedDataDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightObservedDataModule {}
