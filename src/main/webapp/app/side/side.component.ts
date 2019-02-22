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
    _sideElement: string;
    target: string;

    constructor(private activatedRoute: ActivatedRoute) {
        super();
        this.target = this.activatedRoute.snapshot.data['target'];
        this._sideElement = this.target === 'map' ? 'EVENT_THREAD' : this.target === 'network' ? 'EVENT_THREAD' : '';
    }

    ngOnInit() {}

    ngOnDestroy() {}

    changeSideElement(value: string) {
        this._sideElement = value;
    }
}
