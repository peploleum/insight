import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivityPattern } from 'app/shared/model/activity-pattern.model';

@Component({
    selector: 'jhi-activity-pattern-detail',
    templateUrl: './activity-pattern-detail.component.html'
})
export class ActivityPatternDetailComponent implements OnInit {
    activityPattern: IActivityPattern;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ activityPattern }) => {
            this.activityPattern = activityPattern;
        });
    }

    previousState() {
        window.history.back();
    }
}
