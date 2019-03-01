/**
 * Created by gFolgoas on 28/01/2019.
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'ins-side-buttons',
    templateUrl: './side-buttons.component.html'
})
export class SideButtonsComponent implements OnInit {
    @Input()
    isClosed: boolean;
    @Input()
    _sideElement: string;
    @Input()
    target: string;

    @Output()
    closeEmitter: EventEmitter<boolean> = new EventEmitter();
    @Output()
    changeSideElementEmitter: EventEmitter<string> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    closePanel(doClose: boolean) {
        console.log(this.target);
        this.closeEmitter.emit(doClose);
    }

    changeSideElement(element: string) {
        this.changeSideElementEmitter.emit(element);
    }
}
