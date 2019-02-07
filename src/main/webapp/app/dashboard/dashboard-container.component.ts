/**
 * Created by gFolgoas on 07/02/2019.
 */
import { Component, OnInit } from '@angular/core';
import { EntityMappingInfo, IKibanaDashboardGenerationParameters, KibanaDashboardReference } from 'app/dashboard/kibana-object.model';
import { DashboardService } from 'app/dashboard/dashboard.service';
import { HttpResponse } from '@angular/common/http';
import { AnalysisState } from '../shared/model/analysis.model';

@Component({
    selector: 'ins-dashboard-container',
    templateUrl: './dashboard-container.component.html',
    styles: [':host { width:100% }']
})
export class DashboardContainerComponent implements OnInit {
    dashboardIds: KibanaDashboardReference[] = [];
    mappingInfo: EntityMappingInfo[] = [];
    analysisState: AnalysisState;

    constructor(private dbs: DashboardService) {}

    ngOnInit() {
        this.dbs.dashboards.subscribe(dbs => (this.dashboardIds = dbs));
        this.dbs.mappingInfos.subscribe(maps => (this.mappingInfo = maps));
        this.dbs.analysisState.subscribe(state => (this.analysisState = state));
    }

    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters) {
        if (dashboardParam == null) {
            return;
        }
        this.dbs.postDashboard(dashboardParam).subscribe(
            (res: HttpResponse<string[]>) => {
                console.log('PostDefaultDashboard Succeed');
                if (!res.body || res.body.length === 0) {
                    return;
                }
                this.dbs.onNewDashboardIdReceived(res.body);
            },
            error => {
                this.onError('PostDefaultDashboard Failed');
            }
        );
    }
}
