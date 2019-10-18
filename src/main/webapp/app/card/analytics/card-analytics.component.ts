import { Component, Input, OnInit } from '@angular/core';
import { BiographicsScoreDTO, IHitDTO, IMotClefUrls, Theme } from 'app/shared/model/analytics.model';
import { HttpClient } from '@angular/common/http';
import { DictionaryService } from 'app/dictionary/dictionary.service';

@Component({
    selector: 'ins-card-analytics',
    templateUrl: './card-analytics.component.html',
    styles: []
})
export class CardAnalyticsComponent implements OnInit {
    private _biographicsScore: BiographicsScoreDTO;
    currentTab: Theme = 'TER';

    constructor(protected http: HttpClient, private _ds: DictionaryService) {}

    ngOnInit() {
        this._biographicsScore.idDictionary = this._biographicsScore.scores[0].idDictionary;
        this._ds.getName(this._biographicsScore.idDictionary).subscribe(res => {
            this._biographicsScore.nameDictionary = res;
        });
    }

    onChangeTab(theme) {
        this.currentTab = theme;
    }

    goToLink(url: string) {
        window.open(url, '_blank');
    }

    @Input()
    set biographicsScore(value: BiographicsScoreDTO) {
        value.hits.forEach(h => {
            h.motClefUrls.forEach(m => (m.display = false));
        });
        this._biographicsScore = value;
    }

    get biographicsScore(): BiographicsScoreDTO {
        return this._biographicsScore;
    }

    onLabelClick(mcu: IMotClefUrls, hits: IHitDTO) {
        hits.motClefUrls.forEach(m => (m.display = m === mcu ? !m.display : false));
    }
}
