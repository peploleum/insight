import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IBiographics } from 'app/shared/model/biographics.model';
import { AnalyticsService } from './analytics.service';
import { BiographicsScoreDTO, IHitDTO, ScoreDTO, Theme } from '../shared/model/analytics.model';
import { GenericModel } from 'app/shared/model/generic.model';
import { BASE64URI } from 'app/shared/util/insight-util';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { QuickViewService } from 'app/side/quick-view.service';

@Component({
    selector: 'ins-analytics',
    templateUrl: './analytics.component.html',
    styles: [':host { width:100% }'],
    styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent implements OnInit, OnDestroy {
    biographicsScores: BiographicsScoreDTO[] = [];
    biographics: IBiographics[];

    gridView = false;
    page = 1;
    resultParam: { totalItems: number; query: string; size: number; page: number };

    constructor(
        protected analyticsService: AnalyticsService,
        protected jhiAlertService: JhiAlertService,
        protected activatedRoute: ActivatedRoute,
        protected dataUtils: JhiDataUtils,
        protected router: Router,
        private qv: QuickViewService
    ) {}

    ngOnInit() {}

    ngOnDestroy() {}

    onNewResultParam(param) {
        this.resultParam = param;
        this.page = this.resultParam.page + 1;
    }

    loadPage(p: number) {
        this.page = p;
    }

    getAlertThreshold() {
        return this.analyticsService.alertThreshold;
    }

    onResultQueryReceived(entities: GenericModel[]) {
        this.biographics = entities as IBiographics[];
        this.getScores();
        this.biographics.forEach(b => this.getBioImage(b.id));
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    getScores() {
        this.biographicsScores = [];
        this.biographics.forEach((b: IBiographics) => {
            if (b.externalId) {
                this.analyticsService.getScores(b.externalId).subscribe(
                    (score: ScoreDTO[]) => {
                        const hits = {};
                        score.forEach(s => {
                            s.scoreListMotsClefs.forEach((i: { theme: Theme; motClef: string }) => {
                                if (hits.hasOwnProperty(i.theme)) {
                                    (hits[i.theme] as string[]).push(i.motClef);
                                } else {
                                    hits[i.theme] = [i.motClef];
                                }
                            });
                        });
                        this.biographicsScores.push({
                            biographic: b,
                            hits: Object.keys(hits).map(k => {
                                return { theme: k, motsClefs: hits[k] };
                            }) as IHitDTO[],
                            scores: score
                        });
                        console.log(this.biographicsScores);
                    },
                    error => {
                        console.log('[ANALYTICS] Error lors de la récupération des voisins.');
                    }
                );
            }
        });
    }

    getBioImage(bioIdMongo: string) {
        this.qv
            .find(bioIdMongo)
            .pipe(
                map((res: HttpResponse<GenericModel>) => res.body as IBiographics),
                map((bio: IBiographics) => bio.biographicsImage)
            )
            .subscribe(image => {
                if (image) {
                    const bio = this.biographics.find(b => b.id === bioIdMongo);
                    if (bio) {
                        bio.biographicsImage = image;
                    }
                }
            });
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
