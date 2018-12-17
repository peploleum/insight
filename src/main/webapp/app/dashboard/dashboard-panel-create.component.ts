import {Component, Input, OnInit} from '@angular/core';
import {debounceTime} from 'rxjs/operators';
import {ControlContainer, FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import {
    EntityMappingInfo,
    IEntityMappingInfo,
    IKibanaVisualisationGenerationParameters
} from 'app/dashboard/kibana-object.model';

@Component({
    selector: 'ins-dashboard-panel-create',
    templateUrl: './dashboard-panel-create.component.html',
    styles: [],
    viewProviders: [{provide: ControlContainer, useExisting: FormGroupDirective}]
})
export class DashboardPanelCreateComponent implements OnInit {

    @Input() visualisationParameters: IKibanaVisualisationGenerationParameters;
    @Input() visualisationIndex: number;
    @Input() mappingInfo: EntityMappingInfo[];
    visualisationCreationForm: FormGroup;
    visualisationCreationFormName: string;

    constructor(private parent: FormGroupDirective, private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        this.visualisationCreationForm = this.formBuilder.group({
            visualizationTitle: ['Default Title', Validators.required],
            indexPatternId: ['', Validators.required],
            indexPatternFieldTarget: ['', Validators.required]
        });
        if (this.visualisationParameters.visualizationType === 'VISU_TIMELINE') {
            const timeFromFilter = new FormControl('', Validators.required);
            const timeToFilter = new FormControl('', Validators.required);
            const timeInterval = new FormControl('');
            this.visualisationCreationForm.addControl('timeFromFilter', timeFromFilter);
            this.visualisationCreationForm.addControl('timeToFilter', timeToFilter);
            this.visualisationCreationForm.addControl('timeInterval', timeInterval);
        }
        this.visualisationCreationFormName = 'Visualisation_' + this.visualisationIndex;
        this.parent.form.addControl(this.visualisationCreationFormName, this.visualisationCreationForm);
        this.visualisationCreationForm.valueChanges.pipe(debounceTime(200)).subscribe(value => {
            this.visualisationParameters.visualizationTitle = value.visualizationTitle;
            this.visualisationParameters.indexPatternId = value.indexPatternId;
            this.visualisationParameters.indexPatternFieldTarget = value.indexPatternFieldTarget;
            this.visualisationParameters.timeFromFilter = value.hasOwnProperty('timeFromFilter') ? value.timeFromFilter : '';
            this.visualisationParameters.timeToFilter = value.hasOwnProperty('timeToFilter') ? value.timeToFilter : '';
            this.visualisationParameters.timeInterval = value.hasOwnProperty('timeInterval') ? value.timeInterval : '';
        });
    }

    get f() {
        return this.visualisationCreationForm.controls;
    }

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
