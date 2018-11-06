import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';

@Component({
    selector: 'jhi-intrusion-set-detail',
    templateUrl: './intrusion-set-detail.component.html'
})
export class IntrusionSetDetailComponent implements OnInit {
    intrusionSet: IIntrusionSet;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ intrusionSet }) => {
            this.intrusionSet = intrusionSet;
        });
    }

    previousState() {
        window.history.back();
    }
}
