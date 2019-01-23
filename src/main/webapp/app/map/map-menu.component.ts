/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'ins-map-menu',
    templateUrl: './map-menu.component.html'
})
export class MapMenuComponent implements OnInit {
    @Input()
    networkStates;
    @Input()
    right: string;
    @Input()
    top: string;

    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }
}
