import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IObservedData } from 'app/shared/model/observed-data.model';

@Component({
    selector: 'jhi-observed-data-detail',
    templateUrl: './observed-data-detail.component.html'
})
export class ObservedDataDetailComponent implements OnInit {
    observedData: IObservedData;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ observedData }) => {
            this.observedData = observedData;
        });
    }

    previousState() {
        window.history.back();
    }
}
