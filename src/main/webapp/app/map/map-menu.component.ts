/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MapState } from '../shared/util/map-utils';
import { MapService } from './map.service';
import { Subscription } from 'rxjs/index';

@Component({
    selector: 'ins-map-menu',
    templateUrl: './map-menu.component.html'
})
export class MapMenuComponent implements OnInit, OnDestroy {
    mapStates: MapState;
    @Input()
    left: string;
    @Input()
    bottom: string;

    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    mapStatesSubs: Subscription;

    constructor(private ms: MapService) {}

    ngOnInit() {
        this.mapStatesSubs = this.ms.mapStates.subscribe(newState => (this.mapStates = newState));
    }

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }

    ngOnDestroy() {
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }
    }
}
