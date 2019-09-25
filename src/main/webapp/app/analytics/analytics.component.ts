import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IBiographics } from 'app/shared/model/biographics.model';
import { AnalyticsService } from './analytics.service';
import { BiographicsScoreDTO, IHitDTO, ScoreDTO, Theme } from '../shared/model/analytics.model';
import { GenericModel } from 'app/shared/model/generic.model';
import { BASE64URI } from 'app/shared/util/insight-util';

@Component({
    selector: 'ins-analytics',
    templateUrl: './analytics.component.html',
    styles: [':host { width:100% }']
})
export class AnalyticsComponent implements OnInit, OnDestroy {
    biographicsScores: BiographicsScoreDTO[] = [];
    biographics: IBiographics[];
    selectedBiographic: IBiographics;

    fileToUpload: File = null;

    constructor(
        protected analyticsService: AnalyticsService,
        protected jhiAlertService: JhiAlertService,
        protected activatedRoute: ActivatedRoute,
        protected dataUtils: JhiDataUtils,
        protected router: Router
    ) {}

    ngOnInit() {}

    ngOnDestroy() {}

    onDataSelected(entity: GenericModel) {
        this.selectedBiographic = entity as IBiographics;
    }

    onResultQueryReceived(entities: GenericModel[]) {
        this.biographics = entities as IBiographics[];
        this.generateTESSCO(this.biographics);
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    generateTESSCO(bios: IBiographics[]) {
        this.biographicsScores = [];
        bios.forEach((b: IBiographics) => {
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
                    },
                    error => {
                        console.log('[ANALYTICS] Error lors de la récupération des voisins.');
                    }
                );
            }
        });
    }

    hasHitOnTheme(hits: IHitDTO[], theme: 'TER' | 'ESP' | 'SAB' | 'SUB' | 'CRO'): boolean {
        return !!hits.find(h => h.theme === theme);
    }

    // récupère que le 1er fichier
    handleFileInput(files: FileList) {
        this.fileToUpload = files.item(0);
        console.log(this.fileToUpload);
        this.uploadFileToActivity();
    }

    uploadFileToActivity() {
        this.analyticsService.postFile(this.fileToUpload).subscribe(
            data => {
                console.log('bonjour');
                console.log(data);
            },
            error => {
                console.log(error);
            }
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
