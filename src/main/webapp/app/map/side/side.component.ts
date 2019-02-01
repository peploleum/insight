/**
 * Created by gFolgoas on 01/02/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';

@Component({
    selector: 'ins-side',
    templateUrl: './side.component.html'
})
export class SideComponent extends SideInterface implements OnInit, OnDestroy {
    _showEventThread = true;

    constructor() {
        super();
    }

    ngOnInit() {}

    ngOnDestroy() {}

    showEventThread(value: boolean) {
        this._showEventThread = value;
    }
}
