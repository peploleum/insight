/**
 * Created by gFolgoas on 20/02/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';

@Component({
    selector: 'ins-network-search',
    templateUrl: './network-search.component.html'
})
export class NetworkSearchComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    constructor() {
        super();
    }

    ngOnInit() {}

    ngAfterViewInit() {}

    ngOnDestroy() {}
}
