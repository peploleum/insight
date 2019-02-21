/**
 * Created by gFolgoas on 20/02/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';

@Component({
    selector: 'ins-network-content',
    templateUrl: './network-content.component.html'
})
export class NetworkContentComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    constructor() {
        super();
    }

    ngOnInit() {}

    ngAfterViewInit() {}

    ngOnDestroy() {}
}
