import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {
    EntityMappingInfo,
    IKibanaVisualisationGenerationParameters,
    KibanaDashboardGenerationParameters,
    KibanaVisualisationGenerationParameters,
    VisualizationType
} from 'app/dashboard/kibana-object.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {JhiAlertService} from 'ng-jhipster';
import {debounceTime} from 'rxjs/operators';

@Component({
    selector: 'ins-dashboard-create',
    templateUrl: './dashboard-create.component.html',
    styles: []
})
export class DashboardCreateComponent implements OnInit {

    dashboardParameters: KibanaDashboardGenerationParameters;
    @Input() mappingInfo: EntityMappingInfo[];
    @Output() dashboardEmitter: EventEmitter<KibanaDashboardGenerationParameters> = new EventEmitter();
    createDashboardForm: FormGroup;

    constructor(private jhiAlertService: JhiAlertService, private formBuilder: FormBuilder) {
    }

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
