import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { InsightAttackPatternModule } from './attack-pattern/attack-pattern.module';
import { InsightCampaignModule } from './campaign/campaign.module';
import { InsightCourseOfActionModule } from './course-of-action/course-of-action.module';
import { InsightActorModule } from './actor/actor.module';
import { InsightActivityPatternModule } from './activity-pattern/activity-pattern.module';
import { InsightIntrusionSetModule } from './intrusion-set/intrusion-set.module';
import { InsightMalwareModule } from './malware/malware.module';
import { InsightNetLinkModule } from './net-link/net-link.module';
import { InsightObservedDataModule } from './observed-data/observed-data.module';
import { InsightReportModule } from './report/report.module';
import { InsightThreatActorModule } from './threat-actor/threat-actor.module';
import { InsightToolModule } from './tool/tool.module';
import { InsightVulnerabilityModule } from './vulnerability/vulnerability.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        InsightAttackPatternModule,
        InsightCampaignModule,
        InsightCourseOfActionModule,
        InsightActorModule,
        InsightActivityPatternModule,
        InsightIntrusionSetModule,
        InsightMalwareModule,
        InsightNetLinkModule,
        InsightObservedDataModule,
        InsightReportModule,
        InsightThreatActorModule,
        InsightToolModule,
        InsightVulnerabilityModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightEntityModule {}
