import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { InsightBiographicsModule } from './biographics/biographics.module';
import { InsightOrganisationModule } from './organisation/organisation.module';
import { InsightLocationModule } from './location/location.module';
import { InsightEventModule } from './event/event.module';
import { InsightEquipmentModule } from './equipment/equipment.module';
import { InsightRawDataModule } from './raw-data/raw-data.module';
import { InsightAnalyticsModule } from '../analytics/analytics.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        InsightBiographicsModule,
        InsightOrganisationModule,
        InsightLocationModule,
        InsightEventModule,
        InsightEquipmentModule,
        InsightRawDataModule,
        InsightAnalyticsModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightEntityModule {}
