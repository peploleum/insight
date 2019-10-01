import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { BiographicsScoreDTO, IHitDTO } from '../shared/model/analytics.model';
import { BASE64URI } from 'app/shared/util/insight-util';
import { AnalyticsService } from 'app/analytics/analytics.service';

@Component({
    selector: 'ins-analytics-grid',
    templateUrl: './analytics-grid.component.html',
    styles: [':host { width:100% }'],
    styleUrls: ['./analytics-grid.component.scss']
})
export class AnalyticsGridComponent implements OnInit, OnDestroy {
    @Input()
    biographicsScores: BiographicsScoreDTO[] = [];

    constructor(protected jhiAlertService: JhiAlertService, private _as: AnalyticsService) {}

    ngOnInit() {}

    ngOnDestroy() {}

    getBase64(content: string): string {
        return BASE64URI(content, 'jpeg');
    }

    getHitColor(hits: IHitDTO): string {
        if (!hits || hits.motClefUrls.length === 0) {
            return this._as.alertThreshold[0];
        }
        const nbr = hits.motClefUrls.length;
        return nbr > 1 ? (nbr > 3 ? this._as.alertThreshold[3] : this._as.alertThreshold[2]) : this._as.alertThreshold[1];
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
