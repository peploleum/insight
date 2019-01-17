/**
 * Created by gFolgoas on 17/01/2019.
 */
import { Component, EventEmitter, Inject, OnInit } from '@angular/core';

@Component({
    selector: 'ins-network-side-menu',
    templateUrl: './network-side-menu.component.html'
})
export class NetworkSideMenuComponent implements OnInit {
    networkStates;
    dimension: { top: number; right: number; height: number } = { top: 0, right: 0, height: 0 };

    constructor(@Inject('directiveActionEmitter') private actionEmitter: EventEmitter<string>) {}

    ngOnInit() {}

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }
}
