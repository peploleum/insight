import { Component, OnInit } from '@angular/core';
import { EntityMappingInfo, IEntityMappingInfo, KibanaDashboardReference } from 'app/dashboard/kibana-object.model';
import { JhiAlertService } from 'ng-jhipster';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'ins-dashboard-manager',
    templateUrl: './dashboard-manager.component.html',
    styles: [':host { width:100% }']
})
export class DashboardManagerComponent implements OnInit {
    dashboardIds: KibanaDashboardReference[];

    constructor(private jhiAlertService: JhiAlertService, private dbs: DashboardService) {}

    ngOnInit() {
        this.dbs.dashboards.subscribe(dbIds => {
            this.dashboardIds = dbIds;
        });
        this.dbs.getEntitiesSchema().subscribe(
            (res: HttpResponse<IEntityMappingInfo[]>) => {
                console.log('getEntitiesSchema Succeed');
                const mappingInfos: EntityMappingInfo[] = res.body;
                this.dbs.mappingInfos.next(mappingInfos);
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
        this.getDashboardIds();
    }

    onDashboardSelected(idObject: string) {
        const dbs: KibanaDashboardReference[] = this.dbs.dashboards.getValue();
        dbs.forEach(db => {
            db.selected = db.idObject === idObject;
        });
        this.dbs.dashboards.next(dbs);
    }

    createDashboard() {}

    getDashboardIds() {
        this.dbs.getDashboardIds().subscribe(
            (res: HttpResponse<string[]>) => {
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.dbs.onNewDashboardIdReceived(res.body);
            },
            error => {
                this.onError('Erreurs lors de la récupération des dashboardIds');
            }
        );
    }

    deleteAllDashboard() {
        this.dbs.deleteAllDashboard().subscribe(
            res => {
                console.log('Suppression des dashboards succeed');
                this.dbs.dashboards.next([]);
            },
            error => {
                this.onError('Erreur lors de la suppression des dashboards');
            }
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
