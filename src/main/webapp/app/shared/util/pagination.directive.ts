/**
 * Created by gFolgoas on 23/01/2019.
 */
import { AfterViewInit, Directive, ElementRef, EventEmitter, Input, OnChanges, OnDestroy, Output } from '@angular/core';
import { fromEvent, Subscription } from 'rxjs/index';
import { debounceTime } from 'rxjs/internal/operators';

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
    @Input()
    numberOfItemPerScroll;
    @Input()
    numberOfItemPerPage;

    @Output()
    pageEmitter: EventEmitter<PageInfo> = new EventEmitter();
    page: PageInfo;

    wheelEventSubs: Subscription;

    constructor(private _el: ElementRef) {}

    ngOnChanges(changes: any) {
        this.page = new PageInfo(0, this.numberOfItemPerPage);
    }

    ngAfterViewInit() {
        this.wheelEventSubs = fromEvent(this._el.nativeElement.parentElement, 'wheel')
            .pipe(debounceTime(200))
            .subscribe((e: WheelEvent) => {
                if (this.dataList) {
                    this.page.startIndex =
                        e.deltaY < 0
                            ? Math.max(0, this.page.startIndex - this.numberOfItemPerScroll)
                            : this.page.lastIndex !== this.dataList.length
                            ? Math.min(
                                  this.dataList.length - (1 + this.numberOfItemPerScroll),
                                  this.page.startIndex + this.numberOfItemPerScroll
                              )
                            : this.page.startIndex;
                    this.page.lastIndex =
                        e.deltaY < 0
                            ? Math.max(this.numberOfItemPerPage, this.page.lastIndex - this.numberOfItemPerScroll)
                            : this.page.lastIndex !== this.dataList.length
                            ? Math.min(this.dataList.length, this.page.lastIndex + this.numberOfItemPerScroll)
                            : this.page.lastIndex;
                    this.pageEmitter.emit(this.page);
                }
            });
    }

    ngOnDestroy() {
        if (this.wheelEventSubs) {
            this.wheelEventSubs.unsubscribe();
        }
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
