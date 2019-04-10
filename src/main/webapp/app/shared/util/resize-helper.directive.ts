import { AfterViewInit, Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';

/**
 * Force un HTMLElement à prendre la taille d'un autre identifié par son id
 * */
@Directive({
    selector: '[insResizeHelper]'
})
export class ResizeHelperDirective implements AfterViewInit {
    @Input()
    idTargetElement: string;
    container: HTMLElement;

    @HostListener('window:resize')
    onResize() {
        this.setSize();
    }

    constructor(private _el: ElementRef, private _renderer: Renderer2) {}

    ngAfterViewInit() {
        this.container = document.getElementById(this.idTargetElement);
        this.setSize();
    }

    setSize() {
        if (this.container) {
            const rect: ClientRect = this.container.getBoundingClientRect();
            this._renderer.setStyle(this._el.nativeElement, 'height', `${rect.height - 20}px`);
            this._renderer.setStyle(this._el.nativeElement, 'width', `${rect.width - 20}px`);
        }
    }
}
