import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { DomSanitizer } from '@angular/platform-browser';
import {
    EntityMappingInfo,
    IKibanaVisualisationGenerationParameters,
    KibanaDashboardGenerationParameters,
    KibanaVisualisationGenerationParameters,
    VisualizationType
} from './kibana-object.model';
import { VisualisationService } from './visualisation.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';

@Component({
    selector: 'jhi-visualisation-create-dashboard',
    templateUrl: './visualisation-create-dashboard.component.html'
})
export class VisualisationCreateDashboardComponent implements OnInit, OnDestroy {
    dashboardParameters: KibanaDashboardGenerationParameters;
    @Input() mappingInfo: EntityMappingInfo[];
    @Output() dashboardEmitter: EventEmitter<KibanaDashboardGenerationParameters> = new EventEmitter();
    createDashboardForm: FormGroup;

    constructor(private jhiAlertService: JhiAlertService, private formBuilder: FormBuilder) {}

    ngOnInit() {
        this.dashboardParameters = new KibanaDashboardGenerationParameters('', []);

        this.createDashboardForm = this.formBuilder.group({
            dashboardTitle: ['Dashboard default title', Validators.required]
        });
        this.createDashboardForm.valueChanges.pipe(debounceTime(200)).subscribe(value => {
            this.dashboardParameters.dashboardTitle = value.dashboardTitle;
        });
    }

    get f() {
        return this.createDashboardForm.controls;
    }

    ngOnDestroy() {}

    addVisualisation(visuType: VisualizationType) {
        this.dashboardParameters.visualisations.push(new KibanaVisualisationGenerationParameters('Default Title', visuType, '', '', ''));
    }

    getSelectedIndexPattern(id: string): EntityMappingInfo {
        const matchingPattern: EntityMappingInfo[] = this.mappingInfo.filter((mapInfo: EntityMappingInfo) => {
            return mapInfo.indexPatternId === id;
        });
        return matchingPattern.length > 0 ? matchingPattern[0] : null;
    }

    save() {
        this.dashboardParameters.visualisations.forEach((visu: IKibanaVisualisationGenerationParameters) => {
            visu.indexPatternName = this.getSelectedIndexPattern(visu.indexPatternId).indexPatternName;
        });
        this.dashboardEmitter.emit(this.dashboardParameters);
    }

    cancel() {
        this.dashboardEmitter.emit(null);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
