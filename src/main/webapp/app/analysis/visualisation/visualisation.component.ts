import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { KibanaDashboardReference } from './kibana-object.model';
import { VisualisationService } from './visualisation.service';

@Component({
    selector: 'jhi-visualisation',
    templateUrl: './visualisation.component.html'
})
export class VisualisationComponent implements OnInit, OnDestroy {
    dashboardIds: KibanaDashboardReference[] = [];
    dashboardSafeUrls: SafeResourceUrl[] = [];

    constructor(private jhiAlertService: JhiAlertService, private visuService: VisualisationService, private ds: DomSanitizer) {}

    ngOnInit() {
        this.visuService.getEntitiesSchema().subscribe(
            res => {
                console.log('getEntitiesSchema Succeed');
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }

    postDashboard() {
        this.visuService.regenerateDashboard().subscribe(
            res => {
                console.log('PostDefaultDashboard Succeed');
                this.getDashboardIds();
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }

    getDashboardIds() {
        this.visuService.getDefaultDashboardId().subscribe(
            (res: HttpResponse<string[]>) => {
                console.log('RESPONSE ID DASHBOARD RECEIVED NBRE : ' + res.body.length);
                res.body.forEach((id: string) => {
                    this.dashboardIds.push(new KibanaDashboardReference(id));
                });
                this.dashboardIds.forEach(dbRef => {
                    this.dashboardSafeUrls.push(dbRef.getSafeResourceUrl(this.ds, 'http://192.168.99.100:5601/'));
                });
            },
            (res: HttpErrorResponse) => {
                this.onError('Erreur parsing de getDashboardId');
            }
        );
    }

    ngOnDestroy() {}

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
