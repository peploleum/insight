import { AfterViewInit, Directive, ElementRef, HostListener, Input, Renderer2 } from '@angular/core';

@Directive({
    selector: '[insOverlayPosition]'
})
export class OverlayPositionDirective implements AfterViewInit {
    @Input()
    idTargetElement: string;
    targetContainer: HTMLElement;

    @Input()
    position: InsPositionType;

    @HostListener('window:resize')
    onResize() {
        this.setPosition();
    }

    constructor(private _el: ElementRef, private _renderer: Renderer2) {}

    ngAfterViewInit(): void {
        this.targetContainer = document.getElementById(this.idTargetElement);
        this.setPosition();
    }

    setPosition() {
        if (this.targetContainer) {
            const rect: ClientRect = this.targetContainer.getBoundingClientRect();
            switch (this.position) {
                case 'TOP-LEFT':
                    this._renderer.setStyle(this._el.nativeElement, 'left', `${rect.left + 20}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'top', `${rect.top + 20}px`);
                    break;
                case 'TOP-RIGHT':
                    this._renderer.setStyle(this._el.nativeElement, 'right', `${document.body.clientWidth - rect.right + 20}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'top', `${rect.top + 20}px`);
                    break;
                case 'BOTTOM-RIGHT':
                    this._renderer.setStyle(this._el.nativeElement, 'right', `${document.body.clientWidth - rect.right + 20}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'top', `${rect.top + rect.height - 20}px`);
                    break;
                case 'BOTTOM-LEFT':
                    this._renderer.setStyle(this._el.nativeElement, 'left', `${rect.left + 20}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'top', `${rect.top + rect.height - 20}px`);
                    break;
                case 'COVER-TARGET':
                    this._renderer.setStyle(this._el.nativeElement, 'left', `${this.targetContainer.offsetLeft}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'top', `${this.targetContainer.offsetTop}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'height', `${rect.height}px`);
                    this._renderer.setStyle(this._el.nativeElement, 'width', `${rect.width}px`);
                    break;
                default:
                    break;
            }
        }
    }
}

export type InsPositionType = 'TOP-LEFT' | 'TOP-RIGHT' | 'BOTTOM-RIGHT' | 'BOTTOM-LEFT' | 'COVER-TARGET';
