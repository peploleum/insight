import { Component, OnInit } from '@angular/core';
import { GenericModel } from '../shared/model/generic.model';
import { BASE64URI } from '../shared/util/insight-util';
import { QuickViewService } from '../side/quick-view.service';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/internal/operators';
import { ActivatedRoute, UrlSegment } from '@angular/router';
import { AnalyticsService } from 'app/analytics/analytics.service';
import { BiographicsScoreDTO, IHitDTO, ScoreDTO, Theme } from 'app/shared/model/analytics.model';
import { IBiographics } from 'app/shared/model/biographics.model';

@Component({
    selector: 'ins-card',
    templateUrl: './card.component.html',
    styles: []
})
export class CardComponent implements OnInit {
    entity: GenericModel;
    entityExternalId: string;
    entityAndNeighbors: GenericModel[];
    currentTab = 'info';

    biographicsScore: BiographicsScoreDTO;
    analyticsMode = false;

    constructor(private _qv: QuickViewService, private _ar: ActivatedRoute, private _as: AnalyticsService) {}

    ngOnInit() {
        this._ar.data.subscribe(({ biographics }) => {
            if (biographics) {
                this.onDataSelected(biographics);
                this.analyticsMode = !!this._ar.snapshot.url.find((u: UrlSegment) => u.path === 'analytics');
                if (this.analyticsMode) {
                    this.getScores(this.entity as IBiographics);
                }
            }
        });
    }

    onDataSelected(entity: GenericModel) {
        this.entity = entity;
        this.getImage(this.entity['id']);
        this.entityAndNeighbors = [this.entity];
        this.entityExternalId = this.entity['externalId'];
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    setEntities(ids: string[]) {
        this._qv.findMultiple(ids).subscribe(res => {
            this.entityAndNeighbors = res.body;
        });
    }

    getImage(id: string) {
        this._qv
            .find(id)
            .pipe(map((res: HttpResponse<GenericModel>) => res.body))
            .subscribe(entity => {
                this.entity['biographicsImage'] = entity['biographicsImage'];
                this.entity['biographicsSymbol'] = entity['biographicsSymbol'];
            });
    }

    getScores(bio: IBiographics) {
        this._as.getScores(bio.externalId).subscribe(
            (score: ScoreDTO[]) => {
                const hits = {};
                score.forEach(s => {
                    s.scoreListMotsClefs.forEach((i: { theme: Theme; motClef: string }) => {
                        if (hits.hasOwnProperty(i.theme)) {
                            if (!hits[i.theme][i.motClef]) {
                                hits[i.theme][i.motClef] = [{ url: s.rawDataUrl, depth: s.depthLevel }];
                            } else {
                                hits[i.theme][i.motClef].push({ url: s.rawDataUrl, depth: s.depthLevel });
                            }
                        } else {
                            hits[i.theme] = {};
                            hits[i.theme][i.motClef] = [{ url: s.rawDataUrl, depth: s.depthLevel }];
                        }
                    });
                });
                this.biographicsScore = {
                    biographic: bio,
                    hits: Object.keys(hits).map(t => {
                        return {
                            theme: t,
                            motClefUrls: Object.keys(hits[t]).map(mc => {
                                return { motClef: mc, listUrlDepth: hits[t][mc] };
                            })
                        };
                    }) as IHitDTO[],
                    scores: score
                };
            },
            error => {
                console.log('[ANALYTICS] Error lors de la récupération des voisins.');
            }
        );
    }
}
