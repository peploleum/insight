import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import {
    ToolComponent,
    ToolDetailComponent,
    ToolUpdateComponent,
    ToolDeletePopupComponent,
    ToolDeleteDialogComponent,
    toolRoute,
    toolPopupRoute
} from './';

const ENTITY_STATES = [...toolRoute, ...toolPopupRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ToolComponent, ToolDetailComponent, ToolUpdateComponent, ToolDeleteDialogComponent, ToolDeletePopupComponent],
    entryComponents: [ToolComponent, ToolUpdateComponent, ToolDeleteDialogComponent, ToolDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightToolModule {}
