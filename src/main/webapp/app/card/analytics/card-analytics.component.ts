import { Component, Input, OnInit } from '@angular/core';
import { BiographicsScoreDTO } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-card-analytics',
    templateUrl: './card-analytics.component.html',
    styles: []
})
export class CardAnalyticsComponent implements OnInit {
    @Input()
    biographicsScore: BiographicsScoreDTO;

    constructor() {}

    ngOnInit() {}
}
