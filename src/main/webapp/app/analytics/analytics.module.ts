import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InsightSharedModule } from 'app/shared';
import { analyticsRoute } from './analytics.route';
import { AnalyticsComponent } from './analytics.component';
import { AnalyticsDetailComponent } from './analytics-detail.component';
import { AnalyticsTableComponent } from 'app/analytics/analytics-table.component';
import { AnalyticsGridComponent } from 'app/analytics/analytics-grid.component';

const ENTITY_STATES = [...analyticsRoute];

@NgModule({
    imports: [InsightSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AnalyticsComponent, AnalyticsDetailComponent, AnalyticsTableComponent, AnalyticsGridComponent],
    entryComponents: [AnalyticsComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightAnalyticsModule {}
