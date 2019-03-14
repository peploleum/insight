import { Directive, ElementRef, EventEmitter, HostBinding, HostListener, Output } from '@angular/core';

@Directive({
    selector: '[insTextHighlight]'
})
export class TextHighlightDirective {
    @HostBinding('class.green-select')
    validSelection = true;

    @Output()
    selectedTextEmitter: EventEmitter<string> = new EventEmitter();

    @HostListener('mouseup', ['$event'])
    onMouseUp(event: MouseEvent) {
        const target = event.target as HTMLElement;
        if (this._el.nativeElement.contains(target)) {
            const select = this.getSelectedText();
            this.validSelection = this.isValidSelection(select);
            if (this.validSelection) {
                this.selectedTextEmitter.emit(select.toLowerCase());
            }
        }
    }

    constructor(private _el: ElementRef) {}

    getSelectedText(): string {
        const select = window.getSelection ? window.getSelection().toString() : '';
        return select.trim();
    }

    isValidSelection(selection: string): boolean {
        const specialCharacters = /[!@#$%^&*()_+=\[\]{};':"\\|,.<>\/?]/;
        return !specialCharacters.test(selection);
    }
}
