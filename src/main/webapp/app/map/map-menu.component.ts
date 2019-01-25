/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapState } from '../shared/util/map-utils';

@Component({
    selector: 'ins-map-menu',
    templateUrl: './map-menu.component.html'
})
export class MapMenuComponent implements OnInit {
    @Input()
    mapStates: MapState;
    @Input()
    left: string;
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
