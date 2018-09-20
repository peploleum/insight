import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { KibanaDashboardReference, IKibanaVisualisationGenerationParameters } from './kibana-object.model';
import { VisualisationService } from './visualisation.service';

@Component({
    selector: 'jhi-visualisation-create-dashboard',
    templateUrl: './visualisation-create-dashboard.component.html'
})
export class VisualisationCreateDashboard implements OnInit, OnDestroy {
    visualisationParameters: IKibanaVisualisationGenerationParameters;

    constructor(private jhiAlertService: JhiAlertService, private visuService: VisualisationService, private ds: DomSanitizer) {}

    ngOnInit() {}

    ngOnDestroy() {}

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
