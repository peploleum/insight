import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { BiographicsScoreDTO, IHitDTO } from '../shared/model/analytics.model';
import { BASE64URI } from 'app/shared/util/insight-util';

@Component({
    selector: 'ins-analytics-grid',
    templateUrl: './analytics-grid.component.html',
    styles: [':host { width:100% }'],
    styleUrls: ['./analytics-grid.component.scss']
})
export class AnalyticsGridComponent implements OnInit, OnDestroy {
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

    getBase64(content: string): string {
        return BASE64URI(content, 'jpeg');
    }

    getHitColor(hits: IHitDTO): string {
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
