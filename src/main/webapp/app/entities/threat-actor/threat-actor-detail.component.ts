import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IThreatActor } from 'app/shared/model/threat-actor.model';

@Component({
    selector: 'jhi-threat-actor-detail',
    templateUrl: './threat-actor-detail.component.html'
})
export class ThreatActorDetailComponent implements OnInit {
    threatActor: IThreatActor;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ threatActor }) => {
            this.threatActor = threatActor;
        });
    }

    previousState() {
        window.history.back();
    }
}
