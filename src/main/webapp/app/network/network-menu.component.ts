/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { NetworkService } from './network.service';
import { Subscription } from 'rxjs/index';

@Component({
    selector: 'ins-network-menu',
    templateUrl: './network-menu.component.html'
})
export class NetworkMenuComponent implements OnInit, OnDestroy {
    @Input()
    left: string;
    @Input()
    top: string;
    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    networkStates;
    stateSubs: Subscription;

    constructor(private _ns: NetworkService) {}

    ngOnInit() {
        this.stateSubs = this._ns.networkState.subscribe(state => (this.networkStates = state));
    }

    ngOnDestroy() {
        if (this.stateSubs) {
            this.stateSubs.unsubscribe();
        }
    }

    sendAction(action: string) {
        this.actionEmitter.emit(action);
    }
}
