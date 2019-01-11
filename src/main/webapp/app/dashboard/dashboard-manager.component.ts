import { Component, OnInit } from '@angular/core';
import {
    EntityMappingInfo,
    IEntityMappingInfo,
    IKibanaDashboardGenerationParameters,
    KibanaDashboardReference
} from 'app/dashboard/kibana-object.model';
import { JhiAlertService } from 'ng-jhipster';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'ins-dashboard-manager',
    templateUrl: './dashboard-manager.component.html',
    styles: [':host { width:100% }']
})
export class DashboardManagerComponent implements OnInit {
    dashboardIds: KibanaDashboardReference[] = [];
    // dashboardSafeUrls: SafeResourceUrl[] = [];
    selectedDashboardIndex = 0;

    isEditing = false;
    mappingInfo: EntityMappingInfo[] = [];

    constructor(private jhiAlertService: JhiAlertService, private dbs: DashboardService, private ds: DomSanitizer) {}

    ngOnInit() {
        this.dbs.getEntitiesSchema().subscribe(
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
        this.dbs.postDashboard(dashboardParam).subscribe(
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
        this.dbs.getDashboardIds().subscribe(
            (res: HttpResponse<string[]>) => {
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.onNewDashboardIdReceived(res.body);
                /*res.body.forEach((id: string) => {
                    this.dashboardIds.push(new KibanaDashboardReference(id));
                });
                this.dashboardIds.forEach(dbRef => {
                    this.dashboardSafeUrls.push(dbRef.getSafeResourceUrl(this.ds, this.dbs.kibanaUrl));
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
            dbRef.dashboardSafeUrl = dbRef.getSafeResourceUrl(this.ds, this.dbs.kibanaUrl);
            this.dashboardIds.push(dbRef);
        });
    }

    deleteAllDashboard() {
        this.dbs.deleteAllDashboard().subscribe(
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

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
