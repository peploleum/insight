import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    NetLinkComponent,
    NetLinkDetailComponent,
    NetLinkUpdateComponent,
    NetLinkDeletePopupComponent,
    NetLinkDeleteDialogComponent,
    netLinkRoute,
    netLinkPopupRoute
} from './';

const ENTITY_STATES = [...netLinkRoute, ...netLinkPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        NetLinkComponent,
        NetLinkDetailComponent,
        NetLinkUpdateComponent,
        NetLinkDeleteDialogComponent,
        NetLinkDeletePopupComponent
    ],
    entryComponents: [NetLinkComponent, NetLinkUpdateComponent, NetLinkDeleteDialogComponent, NetLinkDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightNetLinkModule {}
