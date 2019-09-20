import { Component, OnDestroy, OnInit } from '@angular/core';
import { EntityMappingInfo, IEntityMappingInfo, KibanaDashboardReference } from 'app/dashboard/kibana-object.model';
import { JhiAlertService } from 'ng-jhipster';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { HttpResponse } from '@angular/common/http';
import { AnalysisState } from '../../shared/model/analytics.model';
import { Subscription } from 'rxjs/index';
import { UUID } from '../../shared/util/insight-util';

@Component({
    selector: 'ins-dashboard-manager',
    templateUrl: './dashboard-manager.component.html',
    styles: [':host { width:100% }']
})
export class DashboardManagerComponent implements OnInit, OnDestroy {
    dashboardIds: KibanaDashboardReference[];
    dbsIdsSubscription: Subscription;

    constructor(private jhiAlertService: JhiAlertService, private dbs: DashboardService) {}

    ngOnInit() {
        this.dbsIdsSubscription = this.dbs.dashboards.subscribe(dbIds => {
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
        this.getDashboardRefs();
    }

    ngOnDestroy() {
        if (this.dbsIdsSubscription) {
            this.dbsIdsSubscription.unsubscribe();
        }
    }

    onDashboardSelected(idObject: string) {
        const dbs: KibanaDashboardReference[] = this.dbs.dashboards.getValue();
        dbs.forEach(db => {
            db.selected = db.idObject === idObject;
        });
        this.dbs.dashboards.next(dbs);
    }

    createDashboard() {
        const currentState: AnalysisState = this.dbs.analysisState.getValue();
        currentState.isEditing = true;
        this.dbs.analysisState.next(currentState);
    }

    getDashboardRefs() {
        this.dbs.getDashboardRefs().subscribe(
            (res: HttpResponse<KibanaDashboardReference[]>) => {
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

    deleteSingleDashboard(id: string) {
        this.dbs.deleteSingleDashboard(id).subscribe(
            res => {
                console.log('Suppression du dashboard succeed');
                const list: KibanaDashboardReference[] = this.dbs.dashboards.getValue();
                const toDelete: KibanaDashboardReference = list.find(i => i.idObject === id);
                if (toDelete) {
                    list.splice(list.indexOf(toDelete), 1);
                    this.dbs.dashboards.next(list);
                }
            },
            error => {
                this.onError('Erreur lors de la suppression du dashboard');
            }
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
