import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { IRawData } from 'app/shared/model/raw-data.model';
import { Subscription } from 'rxjs';
import { RawDataService } from 'app/entities/raw-data';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { MapService } from './map.service';
import { tap } from 'rxjs/internal/operators';
import { EventThreadResultSet } from '../shared/util/map-utils';

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
    currentSearch: string;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: true;

    firstId: string;
    lastId: string;
    numberOfItemPerDomPage = 5;

    firstIndex: string;
    lastIndex: string;

    @Output()
    selectOnMapEmitter: EventEmitter<string> = new EventEmitter();

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
        this.itemsPerPage = 40;
        this.currentSearch = '';
        this.predicate = 'rawDataCreationDate';
        this.page = 1;
    }

    getFirstIndex() {
        let firstIndex = this.rawDataList.dataIds.indexOf(this.firstId);
        if (firstIndex === -1 && this.rawDataList.dataIds.length > 0) {
            this.firstId = this.rawDataList.dataIds[0];
            firstIndex = 0;
        }
        return firstIndex;
    }

    getLastIndex() {
        let lastIndex = this.rawDataList.dataIds.indexOf(this.lastId);
        if (lastIndex === -1 && this.rawDataList.dataIds.length > 0) {
            this.lastId = this.rawDataList.dataIds[
                Math.max(
                    this.numberOfItemPerDomPage < this.rawDataList.dataIds.length - 1
                        ? this.numberOfItemPerDomPage
                        : this.rawDataList.dataIds.length - 1,
                    this.rawDataList.dataIds.indexOf(this.lastId) - 1
                )
            ];
            lastIndex = this.rawDataList.dataIds.indexOf(this.lastId);
        }
        return lastIndex;
    }

    updateIdStore(isFirst: boolean, isLast: boolean, id: string) {
        this.firstId = isFirst ? id : this.firstId;
        this.lastId = isLast ? id : this.lastId;
    }

    loadAll() {
        console.log('loading');
        if (this.currentSearch) {
            this.rawDataService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IRawData[]>) => this.paginateRawData(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        console.log('param ' + (this.page - 1) + ' ' + this.itemsPerPage + ' ' + this.sort());
        this.rawDataService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort(),
                filter: null // out of 'all', 'locations', 'images', no filter in options means retrieving all data
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
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.loadAll();
        }
    }

    transition() {
        this.router.navigate(['/raw-data'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate([
            '/raw-data',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate([
            '/raw-data',
            {
                search: this.currentSearch,
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        ]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRawData();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
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
        this.eventSubscriber = this.eventManager.subscribe('rawDataListModification', response => this.loadAll());
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
        this.rawDataList.data = this.rawDataList.data.concat(data);
        this.rawDataList.dataIds = this.rawDataList.dataIds.concat(data.map(item => item.id));
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

    onNewPage(dir: number) {
        this.firstId =
            dir < 0
                ? this.rawDataList.dataIds[Math.max(0, this.rawDataList.dataIds.indexOf(this.firstId) - 1)]
                : this.rawDataList.dataIds[
                      Math.min(
                          this.rawDataList.dataIds.length - this.numberOfItemPerDomPage,
                          this.rawDataList.dataIds.indexOf(this.firstId) + 1
                      )
                  ];
        this.lastId =
            dir < 0
                ? this.rawDataList.dataIds[
                      Math.max(
                          this.numberOfItemPerDomPage < this.rawDataList.dataIds.length - 1
                              ? this.numberOfItemPerDomPage
                              : this.rawDataList.dataIds.length - 1,
                          this.rawDataList.dataIds.indexOf(this.lastId) - 1
                      )
                  ]
                : this.rawDataList.dataIds[
                      Math.min(this.rawDataList.dataIds.length - 1, this.rawDataList.dataIds.indexOf(this.lastId) + 1)
                  ];
    }

    onBottomPage(event) {
        this.loadPage(this.previousPage + 1);
    }

    selectOnMap(itemId: string) {
        this.selectOnMapEmitter.emit(itemId);
    }
}
