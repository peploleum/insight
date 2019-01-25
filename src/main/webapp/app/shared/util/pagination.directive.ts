/**
 * Created by gFolgoas on 23/01/2019.
 */
import { AfterViewInit, Directive, ElementRef, EventEmitter, Input, OnChanges, OnDestroy, Output } from '@angular/core';
import { fromEvent, Subscription } from 'rxjs/index';
import { debounceTime } from 'rxjs/internal/operators';

/**
 * Emet uniquement la direction du wheelEvent
 *
 */
@Directive({
    selector: '[insPaginationDirective]'
})
export class PaginationDirective implements AfterViewInit, OnChanges, OnDestroy {
    @Input()
    dataList: any[];
    @Output()
    wheelEmitter: EventEmitter<number> = new EventEmitter();
    wheelEventSubs: Subscription;

    constructor(private _el: ElementRef) {}

    ngOnChanges(changes: any) {}

    ngAfterViewInit() {
        this.wheelEventSubs = fromEvent(this._el.nativeElement.parentElement, 'wheel')
            .pipe(debounceTime(200))
            .subscribe((e: WheelEvent) => {
                if (this.dataList) {
                    this.wheelEmitter.emit(e.deltaY);
                }
            });
    }

    ngOnDestroy() {
        if (this.wheelEventSubs) {
            this.wheelEventSubs.unsubscribe();
        }
    }
}
