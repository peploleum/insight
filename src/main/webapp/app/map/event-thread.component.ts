import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { IRawData } from 'app/shared/model/raw-data.model';
import { Subscription } from 'rxjs';
import { RawDataService } from 'app/entities/raw-data';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { MapService } from './map.service';
import { tap } from 'rxjs/internal/operators';
import { EventThreadResultSet, MapState } from '../shared/util/map-utils';

@Component({
    selector: 'ins-event-thread',
    templateUrl: './event-thread.component.html',
    styles: []
})
export class EventThreadComponent implements OnInit, OnDestroy {
    currentAccount: any;
    rawDataList: EventThreadResultSet = new EventThreadResultSet([], []);
    error: any;
    success: any;
    eventSubscriber: Subscription;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    reverse: true;

    firstId: string;
    numberOfItemPerDomPage = 5;

    firstIndex: number;
    lastIndex: number;

    @Output()
    selectOnMapEmitter: EventEmitter<string> = new EventEmitter();
    @Input()
    mapStates: MapState;

    constructor(
        protected rawDataService: RawDataService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected dataUtils: JhiDataUtils,
        protected router: Router,
        protected eventManager: JhiEventManager,
        protected ms: MapService
    ) {
        this.itemsPerPage = 20;
        this.predicate = 'rawDataCreationDate';
        this.page = 1;
    }

    updateIdStore(isFirst: boolean, id: string) {
        this.firstId = isFirst ? id : this.firstId;
        return true;
    }

    loadAll() {
        console.log('loading');
        console.log('param ' + (this.page - 1) + ' ' + this.itemsPerPage + ' ' + this.sort());
        this.rawDataService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                filter: this.mapStates.FILTER_TYPE // out of 'all', 'locations', 'images', no filter in options means retrieving all data
            })
            .pipe(
                tap((res: HttpResponse<IRawData[]>) => {
                    if (res.ok) {
                        // Envoi en carto
                        this.ms.getFeaturesFromRawData(res.body);
                    }
                })
            )
            .subscribe(
                (res: HttpResponse<IRawData[]>) => this.paginateRawData(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.page = 1;
        this.rawDataList.clearAll();
        this.firstId = null;
        this.firstIndex = 0;
        this.lastIndex = this.numberOfItemPerDomPage;
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRawData();
    }

    ngOnDestroy() {
        // this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRawData) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInRawData() {
        // this.eventSubscriber = this.eventManager.subscribe('rawDataListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateRawData(data: IRawData[], headers: HttpHeaders) {
        console.log('loaded ' + data.length);
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        const newDataList: EventThreadResultSet = new EventThreadResultSet(
            this.rawDataList.data.concat(data),
            this.rawDataList.dataIds.concat(data.map(item => item.id))
        );
        this.rawDataList = newDataList;
        if (this.firstId) {
            this.firstIndex = this.rawDataList.dataIds.indexOf(this.firstId);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    getImageIconSrc(objectType: string): string {
        return MapService.getImageIconUrl(objectType);
    }

    getImageBase64Src(content: string): string {
        return `data:image/png;base64,${content}`;
    }

    getSourceTypeIcon(sourceType: string): string {
        switch (sourceType) {
            case 'TWITTER':
                return 'rss-square';
            case 'RSS':
                return 'rss-square';
            default:
                return 'rss-square';
        }
    }

    onNewPage(dir: number) {
        const currentIndex: number = this.rawDataList.dataIds.indexOf(this.firstId);
        const nextId: string = dir > 0 ? this.rawDataList.dataIds[currentIndex + 1] : this.rawDataList.dataIds[currentIndex - 1];
        this.firstIndex = nextId ? this.rawDataList.dataIds.indexOf(nextId) : currentIndex;
        this.lastIndex = this.firstIndex + this.numberOfItemPerDomPage;
        // Si le dernier élément de la liste est le firstId, charge la page suivante.
        if (this.firstIndex === this.rawDataList.dataIds.length - 1) {
            this.loadPage(this.page + 1);
        }
    }

    selectOnMap(itemId: string) {
        this.selectOnMapEmitter.emit(itemId);
    }
}
