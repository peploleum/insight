import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { IRawData } from 'app/shared/model/raw-data.model';
import { Subscription } from 'rxjs';
import { RawDataService } from 'app/entities/raw-data';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { PageInfo } from '../shared/util/pagination.directive';
import { MapService } from './map.service';

@Component({
    selector: 'ins-event-thread',
    templateUrl: './event-thread.component.html',
    styles: []
})
export class EventThreadComponent implements OnInit, OnDestroy {
    currentAccount: any;
    rawDataList: IRawData[];
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

    startIndex = 0;
    lastIndex = 5;

    @Output()
    selectOnMapEmitter: EventEmitter<string> = new EventEmitter();

    constructor(
        protected rawDataService: RawDataService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected dataUtils: JhiDataUtils,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {
        // this.itemsPerPage = ITEMS_PER_PAGE;
        this.itemsPerPage = 40;
        this.currentSearch = '';
        this.predicate = 'rawDataCreationDate';
        this.page = 1;
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
            .subscribe(
                (res: HttpResponse<IRawData[]>) => this.paginateRawData(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
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
        this.rawDataList = data;
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

    onNewPage(newPage: PageInfo) {
        this.startIndex = newPage.startIndex;
        this.lastIndex = newPage.lastIndex;
    }

    selectOnMap(itemId: string) {
        this.selectOnMapEmitter.emit(itemId);
    }
}
