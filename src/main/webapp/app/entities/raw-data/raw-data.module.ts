import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    RawDataComponent,
    RawDataDetailComponent,
    RawDataUpdateComponent,
    RawDataDeletePopupComponent,
    RawDataDeleteDialogComponent,
    rawDataRoute,
    rawDataPopupRoute
} from './';

const ENTITY_STATES = [...rawDataRoute, ...rawDataPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RawDataComponent,
        RawDataDetailComponent,
        RawDataUpdateComponent,
        RawDataDeleteDialogComponent,
        RawDataDeletePopupComponent
    ],
    entryComponents: [RawDataComponent, RawDataUpdateComponent, RawDataDeleteDialogComponent, RawDataDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightRawDataModule {}
