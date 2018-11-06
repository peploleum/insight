import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    CampaignComponent,
    CampaignDetailComponent,
    CampaignUpdateComponent,
    CampaignDeletePopupComponent,
    CampaignDeleteDialogComponent,
    campaignRoute,
    campaignPopupRoute
} from './';

const ENTITY_STATES = [...campaignRoute, ...campaignPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CampaignComponent,
        CampaignDetailComponent,
        CampaignUpdateComponent,
        CampaignDeleteDialogComponent,
        CampaignDeletePopupComponent
    ],
    entryComponents: [CampaignComponent, CampaignUpdateComponent, CampaignDeleteDialogComponent, CampaignDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightCampaignModule {}
