import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { BiographicsScoreDTO, IHitDTO } from '../shared/model/analytics.model';
import { AnalyticsService } from 'app/analytics/analytics.service';

@Component({
    selector: 'ins-analytics-table',
    templateUrl: './analytics-table.component.html',
    styles: [':host { width:100% }'],
    styleUrls: ['./analytics-table.component.scss']
})
export class AnalyticsTableComponent implements OnInit, OnDestroy {
    @Input()
    biographicsScores: BiographicsScoreDTO[] = [];

    constructor(protected jhiAlertService: JhiAlertService, private _as: AnalyticsService) {}

    ngOnInit() {}

    ngOnDestroy() {}

    hasHitOnTheme(hits: IHitDTO[], theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO'): boolean {
        return !!hits.find(h => h.theme === theme);
    }

    getHitColor(h: IHitDTO[], theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO') {
        const hits = h.find(i => i.theme === theme);
        if (!hits || hits.motsClefs.length === 0) {
            return this._as.alertThreshold[0];
        }
        const nbr = hits.motsClefs.length;
        return nbr > 1 ? (nbr > 3 ? this._as.alertThreshold[3] : this._as.alertThreshold[2]) : this._as.alertThreshold[1];
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
