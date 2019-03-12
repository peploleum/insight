/**
 * Created by gFolgoas on 12/03/2019.
 */
import { Component, Input, OnDestroy, OnInit } from '@angular/core';

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

    constructor() {}

    ngOnInit() {}

    ngOnDestroy() {}

    sendAction(action: string) {}
}
