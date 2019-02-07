/**
 * Created by gFolgoas on 01/02/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../shared/side/side.abstract';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'ins-side',
    templateUrl: './side.component.html'
})
export class SideComponent extends SideInterface implements OnInit, OnDestroy {
    _showEventThread = true;
    target: string;

    constructor(private activatedRoute: ActivatedRoute) {
        super();
        this.target = this.activatedRoute.snapshot.data['target'];
    }

    ngOnInit() {}

    ngOnDestroy() {}

    showEventThread(value: boolean) {
        this._showEventThread = value;
    }
}
