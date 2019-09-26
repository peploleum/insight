import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { BiographicsScoreDTO, IHitDTO } from '../shared/model/analytics.model';

@Component({
    selector: 'ins-analytics-table',
    templateUrl: './analytics-table.component.html',
    styles: [':host { width:100% }'],
    styleUrls: ['./analytics-table.component.scss']
})
export class AnalyticsTableComponent implements OnInit, OnDestroy {
    @Input()
    biographicsScores: BiographicsScoreDTO[] = [];

    alertThreshold = {
        0: 'green',
        1: 'yellow',
        2: 'orange',
        3: 'red'
    };

    constructor(protected jhiAlertService: JhiAlertService) {}

    ngOnInit() {}

    ngOnDestroy() {}

    hasHitOnTheme(hits: IHitDTO[], theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO'): boolean {
        return !!hits.find(h => h.theme === theme);
    }

    getHitColor(h: IHitDTO[], theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO') {
        const hits = h.find(i => i.theme === theme);
        if (!hits || hits.motsClefs.length === 0) {
            return this.alertThreshold[0];
        }
        const nbr = hits.motsClefs.length;
        return nbr > 1 ? (nbr > 3 ? this.alertThreshold[3] : this.alertThreshold[2]) : this.alertThreshold[1];
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
