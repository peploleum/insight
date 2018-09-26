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
    // dashboardSafeUrls: SafeResourceUrl[] = [];
    selectedDashboardIndex = 0;

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
        this.getDashboardIds();
    }

    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters) {
        if (dashboardParam == null) {
            this.isEditing = false;
            return;
        }
        this.visuService.postDashboard(dashboardParam).subscribe(
            (res: HttpResponse<string[]>) => {
                console.log('PostDefaultDashboard Succeed');
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.onNewDashboardIdReceived(res.body);
                this.isEditing = false;
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }

    createDashboard() {
        this.isEditing = true;
    }

    getDashboardIds() {
        this.visuService.getDashboardIds().subscribe(
            (res: HttpResponse<string[]>) => {
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.onNewDashboardIdReceived(res.body);
                /*res.body.forEach((id: string) => {
                    this.dashboardIds.push(new KibanaDashboardReference(id));
                });
                this.dashboardIds.forEach(dbRef => {
                    this.dashboardSafeUrls.push(dbRef.getSafeResourceUrl(this.ds, this.visuService.kibanaUrl));
                });*/
                this.isEditing = false;
            },
            error => {
                this.onError('Erreurs lors de la récupération des dashboardIds');
            }
        );
    }

    onNewDashboardIdReceived(idList: string[]) {
        this.dashboardIds = [];
        idList.forEach(id => {
            const dbRef = new KibanaDashboardReference(id);
            dbRef.dashboardSafeUrl = dbRef.getSafeResourceUrl(this.ds, this.visuService.kibanaUrl);
            this.dashboardIds.push(dbRef);
        });
    }

    deleteAllDashboard() {
        this.visuService.deleteAllDashboard().subscribe(
            res => {
                console.log('Suppression des dashboards succeed');
                this.dashboardIds = [];
            },
            error => {
                this.onError('Erreur lors de la suppression des dashboards');
            }
        );
    }

    goToNextDashboard() {
        if (this.selectedDashboardIndex + 1 < this.dashboardIds.length) {
            this.selectedDashboardIndex++;
        }
    }

    goToPreviousDashboard() {
        if (this.selectedDashboardIndex - 1 > -1) {
            this.selectedDashboardIndex--;
        }
    }

    ngOnDestroy() {}

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
