/**
 * Created by gFolgoas on 28/01/2019.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'ins-side-close',
    templateUrl: './side-close.component.html'
})
export class SideCloseComponent implements OnInit {
    @Input()
    isClosed: boolean;
    @Input()
    _showEventThread: boolean;

    @Output()
    closeEmitter: EventEmitter<boolean> = new EventEmitter();
    @Output()
    showEventThreadEmitter: EventEmitter<boolean> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    closePanel(doClose: boolean) {
        this.closeEmitter.emit(doClose);
    }

    showEventThread(show: boolean) {
        this.showEventThreadEmitter.emit(show);
    }
}
