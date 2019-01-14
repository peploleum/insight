/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
    selector: 'ins-network-menu',
    templateUrl: './network-menu.component.html'
})
export class NetworkMenuComponent implements OnInit {
    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }
}
