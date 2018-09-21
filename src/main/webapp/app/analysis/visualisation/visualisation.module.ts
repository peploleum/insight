import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import { visualisationRoute } from './visualisation.route';
import { VisualisationComponent } from './visualisation.component';
import { VisualisationCreateDashboardComponent } from 'app/analysis/visualisation/visualisation-create-dashboard.component';
import { VisualisationCreateVisualisationDashboardComponent } from 'app/analysis/visualisation/visualisation-create-visualisation.component';

const ENTITY_STATES = [...visualisationRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [VisualisationComponent, VisualisationCreateDashboardComponent, VisualisationCreateVisualisationDashboardComponent],
    entryComponents: [VisualisationComponent, VisualisationCreateDashboardComponent, VisualisationCreateVisualisationDashboardComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightSharedVisualisationModule {}
