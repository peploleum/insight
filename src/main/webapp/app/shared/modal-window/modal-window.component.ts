/**
 * Created by gFolgoas on 12/03/2019.
 */
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';

@Component({
    selector: 'ins-modal-window',
    templateUrl: './modal-window.component.html'
})
export class ModalWindowComponent implements OnInit, OnDestroy {
    @Input()
    titleModal: string;
    @Input()
    modalBottom;
    @Input()
    modalLeft;
    @Input()
    expanded = true;

    @Output()
    expansionClickEmitter: EventEmitter<boolean> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    ngOnDestroy() {}

    onClick() {
        this.expansionClickEmitter.emit(true);
    }
}
