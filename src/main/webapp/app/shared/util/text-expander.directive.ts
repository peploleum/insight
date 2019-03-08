/**
 * Created by gFolgoas on 28/02/2019.
 */
import { AfterViewInit, Directive, ElementRef, HostListener, Input, OnDestroy, OnInit, Renderer2 } from '@angular/core';

/**
 * Permet d'étendre ou fermer une zone de texte
 */
@Directive({
    selector: '[insTextExpanderDirective]'
})
export class TextExpanderDirective implements OnInit, AfterViewInit, OnDestroy {
    @Input()
    txtLimit: number; // nombre de caractères maximum en mode fermé
    expanded = false;
    txtContent: string;

    constructor(private _el: ElementRef, private _renderer: Renderer2) {}

    ngOnInit() {}

    ngAfterViewInit() {
        this.txtContent = this._el.nativeElement.innerHTML;
        this.setContent();
    }

    ngOnDestroy() {}

    setContent() {
        if (this.txtContent) {
            const newtext: string =
                this.txtContent.length > this.txtLimit && !this.expanded
                    ? this.txtContent.substring(0, this.txtLimit).concat('...')
                    : this.txtContent;
            this._renderer.setProperty(this._el.nativeElement, 'innerHTML', newtext);
        }
    }

    @HostListener('click', ['$event'])
    onClick(event: MouseEvent) {
        const target = event.target as HTMLElement;
        // Ne pas effectuer d'action sur un HTML tag <a>
        if (target.tagName === 'A') {
            return;
        }
        this.expanded = !this.expanded;
        this.setContent();
    }
}
