import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    AttackPatternComponent,
    AttackPatternDetailComponent,
    AttackPatternUpdateComponent,
    AttackPatternDeletePopupComponent,
    AttackPatternDeleteDialogComponent,
    attackPatternRoute,
    attackPatternPopupRoute
} from './';

const ENTITY_STATES = [...attackPatternRoute, ...attackPatternPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AttackPatternComponent,
        AttackPatternDetailComponent,
        AttackPatternUpdateComponent,
        AttackPatternDeleteDialogComponent,
        AttackPatternDeletePopupComponent
    ],
    entryComponents: [
        AttackPatternComponent,
        AttackPatternUpdateComponent,
        AttackPatternDeleteDialogComponent,
        AttackPatternDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightAttackPatternModule {}
