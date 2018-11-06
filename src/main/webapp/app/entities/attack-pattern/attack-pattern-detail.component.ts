import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAttackPattern } from 'app/shared/model/attack-pattern.model';

@Component({
    selector: 'jhi-attack-pattern-detail',
    templateUrl: './attack-pattern-detail.component.html'
})
export class AttackPatternDetailComponent implements OnInit {
    attackPattern: IAttackPattern;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ attackPattern }) => {
            this.attackPattern = attackPattern;
        });
    }

    previousState() {
        window.history.back();
    }
}
