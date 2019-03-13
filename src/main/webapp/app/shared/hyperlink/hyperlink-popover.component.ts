/**
 * Created by gFolgoas on 11/03/2019.
 */
import { AfterViewInit, Component, EventEmitter, HostListener, Inject, OnChanges, OnDestroy, OnInit } from '@angular/core';
import { toKebabCase } from '../util/insight-util';

@Component({
    selector: 'ins-hyperlink-popover',
    templateUrl: './hyperlink-popover.component.html'
})
export class HyperlinkPopoverComponent implements OnChanges, OnInit, OnDestroy, AfterViewInit {
    idMongo: string;
    idJanus: string;
    entityType: string;

    right: number;
    top: number;

    @HostListener('mouseleave', ['$event'])
    onMouseLeave(event: MouseEvent) {
        this._closeEmitter.emit(true);
    }

    @HostListener('document:click', ['$event'])
    onMouseGlobalClick(event: MouseEvent) {
        const target = event.target as HTMLElement;
        if (this.canClose(target)) {
            this._closeEmitter.emit(true);
        }
    }

    constructor(@Inject('closeEmitter') private _closeEmitter: EventEmitter<boolean>) {}

    ngOnChanges(changes: any) {}

    ngAfterViewInit() {}

    ngOnInit() {
        if (this.entityType) {
        }
    }

    ngOnDestroy() {}

    canClose(target: HTMLElement): boolean {
        return (
            !target.classList.contains('ins-hyperlink') &&
            !target.classList.contains('event') &&
            !target.classList.contains('location') &&
            !target.classList.contains('equipment') &&
            !target.classList.contains('rawdata') &&
            !target.classList.contains('biographics')
        );
    }

    getLink(): string {
        const i: string = toKebabCase(this.entityType);
        return '/' + i;
    }
}
