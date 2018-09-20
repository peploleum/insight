import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    BiographicsComponent,
    BiographicsDetailComponent,
    BiographicsUpdateComponent,
    BiographicsDeletePopupComponent,
    BiographicsDeleteDialogComponent,
    biographicsRoute,
    biographicsPopupRoute
} from './';

const ENTITY_STATES = [...biographicsRoute, ...biographicsPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BiographicsComponent,
        BiographicsDetailComponent,
        BiographicsUpdateComponent,
        BiographicsDeleteDialogComponent,
        BiographicsDeletePopupComponent
    ],
    entryComponents: [BiographicsComponent, BiographicsUpdateComponent, BiographicsDeleteDialogComponent, BiographicsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightBiographicsModule {}
