import { Directive, ElementRef, EventEmitter, HostBinding, HostListener, Output } from '@angular/core';

@Directive({
    selector: '[insTextHighlight]'
})
export class TextHighlightDirective {
    @HostBinding('class.green-select')
    validSelection = true;

    @Output()
    selectedTextEmitter: EventEmitter<{ textSelected: string; position: number }> = new EventEmitter();

    @HostListener('mouseup', ['$event'])
    onMouseUp(event: MouseEvent) {
        const target = event.target as HTMLElement;
        if (this._el.nativeElement.contains(target)) {
            const select = this.getSelectedText();
            this.validSelection = this.isValidSelection(select);
            if (this.validSelection) {
                const pos: number = this.getSelectionPosition();
                this.selectedTextEmitter.emit({ textSelected: select.toLowerCase(), position: pos });
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

    getSelectionPosition(): number {
        if (window.getSelection) {
            const selection = window.getSelection();

            if (selection.rangeCount) {
                // Get the selected range
                const range = selection.getRangeAt(0);

                // Check that the selection is wholly contained within the element (p, div, span...) text
                // if (range.commonAncestorContainer === this._el.nativeElement.firstChild) {
                // Create a range that spans the content from the start of the div
                // to the start of the selection
                const precedingRange = document.createRange();
                precedingRange.setStartBefore(this._el.nativeElement.firstChild);
                precedingRange.setEnd(range.startContainer, range.startOffset);

                // Get the text preceding the selection and do a crude estimate
                // of the number of words by splitting on white space
                const textPrecedingSelection = precedingRange.toString();
                return textPrecedingSelection.length;
                // }
            }
        }
    }
}
