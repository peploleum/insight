import { Directive, EventEmitter, HostListener, Output } from '@angular/core';
import { DragParameter } from './insight-util';

@Directive({
    selector: '[insDropZone]'
})
export class DropZoneDirective {
    @Output() dropEmitter: EventEmitter<DragParameter> = new EventEmitter();

    @HostListener('window:drop', ['$event'])
    public onDrop(evt) {
        evt.preventDefault();
        evt.stopPropagation();
        const data: string = evt.dataTransfer.getData('text');
        this.dropEmitter.emit(new DragParameter(data, evt));
    }

    @HostListener('window:dragover', ['$event'])
    public onDragOver(evt) {
        evt.preventDefault();
        evt.stopPropagation();
    }

    constructor() {}
}
