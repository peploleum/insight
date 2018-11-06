import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    ThreatActorComponent,
    ThreatActorDetailComponent,
    ThreatActorUpdateComponent,
    ThreatActorDeletePopupComponent,
    ThreatActorDeleteDialogComponent,
    threatActorRoute,
    threatActorPopupRoute
} from './';

const ENTITY_STATES = [...threatActorRoute, ...threatActorPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ThreatActorComponent,
        ThreatActorDetailComponent,
        ThreatActorUpdateComponent,
        ThreatActorDeleteDialogComponent,
        ThreatActorDeletePopupComponent
    ],
    entryComponents: [ThreatActorComponent, ThreatActorUpdateComponent, ThreatActorDeleteDialogComponent, ThreatActorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightThreatActorModule {}
