/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapState } from '../shared/util/map-utils';
import { MapService } from './map.service';

@Component({
    selector: 'ins-map-menu',
    templateUrl: './map-menu.component.html'
})
export class MapMenuComponent implements OnInit {
    mapStates: MapState;
    @Input()
    left: string;
    @Input()
    top: string;

    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    constructor(private ms: MapService) {}

    ngOnInit() {
        this.ms.mapStates.subscribe(newState => (this.mapStates = newState));
    }

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }
}
