import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    OrganisationComponent,
    OrganisationDetailComponent,
    OrganisationUpdateComponent,
    OrganisationDeletePopupComponent,
    OrganisationDeleteDialogComponent,
    organisationRoute,
    organisationPopupRoute
} from './';

const ENTITY_STATES = [...organisationRoute, ...organisationPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        OrganisationComponent,
        OrganisationDetailComponent,
        OrganisationUpdateComponent,
        OrganisationDeleteDialogComponent,
        OrganisationDeletePopupComponent
    ],
    entryComponents: [
        OrganisationComponent,
        OrganisationUpdateComponent,
        OrganisationDeleteDialogComponent,
        OrganisationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightOrganisationModule {}
