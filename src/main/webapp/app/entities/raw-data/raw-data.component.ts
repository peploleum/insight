import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IRawData } from 'app/shared/model/raw-data.model';
import { AccountService } from 'app/core';
import { RawDataService } from './raw-data.service';

@Component({
    selector: 'ins-raw-data',
    templateUrl: './raw-data.component.html'
})
export class RawDataComponent implements OnInit, OnDestroy {
    rawData: IRawData[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected rawDataService: RawDataService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.rawDataService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IRawData[]>) => (this.rawData = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.rawDataService.query().subscribe(
            (res: HttpResponse<IRawData[]>) => {
                this.rawData = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
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

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
