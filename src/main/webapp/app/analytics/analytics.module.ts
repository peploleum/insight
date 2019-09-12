import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import { analyticsRoute } from './analytics.route';
import { AnalyticsComponent } from './analytics.component';
import { AnalyticsDetailComponent } from './analytics-detail.component';

const ENTITY_STATES = [...analyticsRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AnalyticsComponent, AnalyticsDetailComponent],
    entryComponents: [AnalyticsComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightAnalyticsModule {}
