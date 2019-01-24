/**
 * Created by gFolgoas on 23/01/2019.
 */
import { AfterViewInit, Directive, ElementRef, EventEmitter, Input, OnChanges, OnDestroy, Output } from '@angular/core';
import { fromEvent, Subscription } from 'rxjs/index';
import { debounceTime } from 'rxjs/internal/operators';
import { InfiniteScrollEvent } from 'ngx-infinite-scroll';

/**
 * Utilise le wheelEvent sur le DOM Element pour calculer une nouvelle pagination
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
    @Output()
    bottomScrollEmitter: EventEmitter<boolean> = new EventEmitter();
    wheelEventSubs: Subscription;
    scrollEventSubs: Subscription;

    numberOfItemPerScroll = 1;
    numberOfItemPerPage = 5;
    page: PageInfo;

    constructor(private _el: ElementRef) {}

    ngOnChanges(changes: any) {
        this.page = new PageInfo(0, this.numberOfItemPerPage);
    }

    ngAfterViewInit() {
        this.wheelEventSubs = fromEvent(this._el.nativeElement.parentElement, 'wheel')
            .pipe(debounceTime(200))
            .subscribe((e: WheelEvent) => {
                if (this.dataList) {
                    this.wheelEmitter.emit(e.deltaY);
                }
            });
        this.scrollEventSubs = fromEvent(this._el.nativeElement, 'scroll').subscribe(e => {
            const bottomReached = this._el.nativeElement.scrollTop + window.innerHeight === this._el.nativeElement.scrollHeight;
            if (bottomReached) {
                this.bottomScrollEmitter.emit(true);
            }
        });
    }

    ngOnDestroy() {
        if (this.wheelEventSubs) {
            this.wheelEventSubs.unsubscribe();
        }
    }

    computeNewIndex(e: WheelEvent) {
        this.page.startIndex =
            e.deltaY < 0
                ? Math.max(0, this.page.startIndex - this.numberOfItemPerScroll)
                : this.page.lastIndex !== this.dataList.length
                ? Math.min(this.dataList.length - (1 + this.numberOfItemPerScroll), this.page.startIndex + this.numberOfItemPerScroll)
                : this.page.startIndex;
        this.page.lastIndex =
            e.deltaY < 0
                ? Math.max(this.numberOfItemPerPage, this.page.lastIndex - this.numberOfItemPerScroll)
                : this.page.lastIndex !== this.dataList.length
                ? Math.min(this.dataList.length, this.page.lastIndex + this.numberOfItemPerScroll)
                : this.page.lastIndex;
    }
}

export class PageInfo {
    startIndex: number;
    lastIndex: number;

    constructor(startIndex: number, lastIndex: number) {
        this.startIndex = startIndex;
        this.lastIndex = lastIndex;
    }
}
