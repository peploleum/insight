import { Component, Input, OnInit } from '@angular/core';
import { BiographicsScoreDTO, IDictionary, ScoreDTO, Theme } from 'app/shared/model/analytics.model';
import { IBiographics } from 'app/shared/model/biographics.model';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { IGraphyNodeDTO } from 'app/shared/model/node.model';
import { of } from 'rxjs';
import { AnalyticsService, FAKE_SCORE } from 'app/analytics/analytics.service';
import { DictionaryService } from 'app/dictionary/dictionary.service';

@Component({
    selector: 'ins-card-analytics',
    templateUrl: './card-analytics.component.html',
    styles: []
})
export class CardAnalyticsComponent implements OnInit {
    @Input()
    biographicsScore: BiographicsScoreDTO;
    currentTab: Theme = 'TER';

    constructor(protected http: HttpClient, private _ds: DictionaryService) {}

    ngOnInit() {
        console.log(this.biographicsScore);
        this.biographicsScore.idDictionary = this.biographicsScore.scores[0].idDictionary;
        this._ds.getName(this.biographicsScore.idDictionary).subscribe(res => {
            this.biographicsScore.nameDictionary = res;
        });
    }

    onChangeTab(theme) {
        this.currentTab = theme;
    }

    goToLink(url: string) {
        window.open(url, '_blank');
    }
}
