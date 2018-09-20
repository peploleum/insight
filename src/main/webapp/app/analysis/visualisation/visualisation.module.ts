import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import { visualisationRoute } from './visualisation.route';
import { VisualisationComponent } from './visualisation.component';

const ENTITY_STATES = [...visualisationRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [VisualisationComponent],
    entryComponents: [VisualisationComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightSharedVisualisationModule {}
