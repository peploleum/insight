import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { InsightSharedVisualisationModule } from './visualisation/visualisation.module';

@NgModule({
    imports: [InsightSharedVisualisationModule],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightSharedAnalysisModule {}
