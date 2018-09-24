import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import {
    KibanaDashboardReference,
    IEntityMappingInfo,
    IKibanaDashboardGenerationParameters,
    EntityMappingInfo
} from './kibana-object.model';
import { VisualisationService } from './visualisation.service';

@Component({
    selector: 'jhi-visualisation',
    templateUrl: './visualisation.component.html'
})
export class VisualisationComponent implements OnInit, OnDestroy {
    dashboardIds: KibanaDashboardReference[] = [];
    dashboardSafeUrls: SafeResourceUrl[] = [];

    isEditing = false;
    mappingInfo: EntityMappingInfo[] = [];

    constructor(private jhiAlertService: JhiAlertService, private visuService: VisualisationService, private ds: DomSanitizer) {}

    ngOnInit() {
        this.visuService.getEntitiesSchema().subscribe(
            (res: HttpResponse<IEntityMappingInfo[]>) => {
                console.log('getEntitiesSchema Succeed');
                res.body.forEach((entity: IEntityMappingInfo) => {
                    this.mappingInfo.push(<EntityMappingInfo>entity);
                });
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }

    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters) {
        if (dashboardParam == null) {
            return;
        }
        this.visuService.postDashboard(dashboardParam).subscribe(
            (res: HttpResponse<string[]>) => {
                console.log('PostDefaultDashboard Succeed');
                res.body.forEach((id: string) => {
                    this.dashboardIds.push(new KibanaDashboardReference(id));
                });
                this.dashboardIds.forEach(dbRef => {
                    this.dashboardSafeUrls.push(dbRef.getSafeResourceUrl(this.ds, 'http://192.168.99.100:5601/'));
                });
                this.isEditing = false;
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }

    createDashboard() {
        this.isEditing = true;
        console.log(this.mappingInfo);
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
