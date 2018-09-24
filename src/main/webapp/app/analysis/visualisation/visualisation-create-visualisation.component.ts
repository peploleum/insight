import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { EntityMappingInfo, IEntityMappingInfo, IKibanaVisualisationGenerationParameters } from './kibana-object.model';

@Component({
    selector: 'jhi-visualisation-create-visualisation',
    templateUrl: './visualisation-create-visualisation.component.html'
})
export class VisualisationCreateVisualisationDashboardComponent implements OnInit, OnDestroy {
    @Input() visualisationParameters: IKibanaVisualisationGenerationParameters;
    @Input() mappingInfo: EntityMappingInfo[];

    constructor() {}

    ngOnInit() {
        console.log(this.mappingInfo);
    }

    ngOnDestroy() {}

    trackIndexById(index: number, item: IEntityMappingInfo) {
        return item.indexPatternId;
    }

    getSelectedIndexPattern(): EntityMappingInfo {
        const matchingPattern: EntityMappingInfo[] = this.mappingInfo.filter((mapInfo: EntityMappingInfo) => {
            return mapInfo.indexPatternId === this.visualisationParameters.indexPatternId;
        });
        return matchingPattern.length > 0 ? matchingPattern[0] : null;
    }

    previousState() {
        window.history.back();
    }
}
