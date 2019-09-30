import { Component, Input, OnInit } from '@angular/core';
import { BiographicsScoreDTO, Theme } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-card-analytics',
    templateUrl: './card-analytics.component.html',
    styles: []
})
export class CardAnalyticsComponent implements OnInit {
    @Input()
    biographicsScore: BiographicsScoreDTO;
    currentTab: Theme = 'TER';

    constructor() {}

    ngOnInit() {
        console.log(this.biographicsScore);
    }

    onChangeTab() {
        console.log(this.currentTab);
    }
}
