/**
 * Created by gFolgoas on 07/02/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { EntityMappingInfo, IKibanaDashboardGenerationParameters, KibanaDashboardReference } from 'app/dashboard/kibana-object.model';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { HttpResponse } from '@angular/common/http';
import { AnalysisState } from '../shared/model/analysis.model';
import { Subscription } from 'rxjs/index';

@Component({
    selector: 'ins-dashboard-container',
    templateUrl: './dashboard-container.component.html',
    styles: [':host { width:100% }']
})
export class DashboardContainerComponent implements OnInit, OnDestroy {
    dashboardSelected: KibanaDashboardReference;
    mappingInfo: EntityMappingInfo[] = [];
    analysisState: AnalysisState;

    dbsIdsSubscription: Subscription;
    mappingInfosSubscription: Subscription;
    analysisStateSubscription: Subscription;

    constructor(private dbs: DashboardService) {}

    ngOnInit() {
        this.dbsIdsSubscription = this.dbs.dashboards.subscribe(
            (dbs: KibanaDashboardReference[]) => (this.dashboardSelected = dbs.find(db => db.selected))
        );
        this.mappingInfosSubscription = this.dbs.mappingInfos.subscribe(maps => (this.mappingInfo = maps));
        this.analysisStateSubscription = this.dbs.analysisState.subscribe(state => (this.analysisState = state));
    }

    ngOnDestroy() {
        if (this.dbsIdsSubscription) {
            this.dbsIdsSubscription.unsubscribe();
        }
        if (this.mappingInfosSubscription) {
            this.mappingInfosSubscription.unsubscribe();
        }
        if (this.analysisStateSubscription) {
            this.analysisStateSubscription.unsubscribe();
        }
    }

    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters) {
        if (dashboardParam == null) {
            const currentState: AnalysisState = this.dbs.analysisState.getValue();
            currentState.isEditing = false;
            this.dbs.analysisState.next(currentState);
            return;
        }
        this.dbs.postDashboard(dashboardParam).subscribe(
            (res: HttpResponse<KibanaDashboardReference[]>) => {
                console.log('PostDefaultDashboard Succeed');
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.dbs.onNewDashboardIdReceived(res.body);
                const currentState: AnalysisState = this.dbs.analysisState.getValue();
                currentState.isEditing = false;
                this.dbs.analysisState.next(currentState);
            },
            error => {
                console.log('PostDefaultDashboard Failed');
            }
        );
    }
}
