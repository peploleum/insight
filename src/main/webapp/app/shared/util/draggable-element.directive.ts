import { Directive, HostListener, Input } from '@angular/core';

@Directive({
    selector: '[insDraggableElement]'
})
export class DraggableElementDirective {
    @Input() data: string;

    @HostListener('dragstart', ['$event'])
    public onDragStart(evt) {
        if (this.data) {
            evt.dataTransfer.setData('text', this.data);
        }
    }

    constructor() {}
}
