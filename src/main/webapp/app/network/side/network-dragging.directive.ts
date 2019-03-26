import { AfterViewInit, Directive, ElementRef, OnDestroy } from '@angular/core';
import { NetworkService } from '../network.service';
import { fromEvent, Observable, Subscription } from 'rxjs/index';
import { last, map, mergeMap, skipUntil, take, takeLast, takeUntil, tap } from 'rxjs/internal/operators';
import { NodeDTO } from '../../shared/model/node.model';

@Directive({
    selector: '[insNetworkDragging]'
})
export class NetworkDraggingDirective implements AfterViewInit, OnDestroy {
    mouseDown$: Observable<Event>;
    mouseUp$: Observable<Event>;
    mouseMove$: Observable<Event>;
    dropping$: Observable<Event>;
    nodeEmitter$: Observable<NodeDTO>;

    lastHoveredNode: NodeDTO;

    mouseDownSubs: Subscription;
    mouseUpSubs: Subscription;
    mouseMoveSubs: Subscription;
    droppingSubs: Subscription;

    constructor(private _ns: NetworkService, private _el: ElementRef) {}

    ngAfterViewInit() {
        this.nodeEmitter$ = this._ns.hoveredNodeEmitter.asObservable();
        if (this._el) {
            this.mouseDown$ = fromEvent(this._el.nativeElement, 'mousedown');
            this.mouseUp$ = fromEvent(document, 'mouseup');
            this.mouseMove$ = fromEvent(document, 'mousemove');

            this.dropping$ = this.mouseDown$.pipe(
                mergeMap(down => {
                    return this.mouseMove$.pipe(skipUntil(this.mouseUp$.pipe(mergeMap(up => this.nodeEmitter$.pipe(map(node => up))))));
                })
            );

            this.dropping$.subscribe(move => {
                console.log('DRAG ENDED!');
                console.log(this.lastHoveredNode);
            });
        }
    }

    ngOnDestroy() {
        if (this.mouseDownSubs) {
            this.mouseDownSubs.unsubscribe();
        }
        if (this.mouseUpSubs) {
            this.mouseUpSubs.unsubscribe();
        }
        if (this.mouseMoveSubs) {
            this.mouseMoveSubs.unsubscribe();
        }
        if (this.droppingSubs) {
            this.droppingSubs.unsubscribe();
        }
    }
}
