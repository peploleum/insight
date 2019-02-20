import { Component, OnDestroy, OnInit } from '@angular/core';
import { IRawData } from 'app/shared/model/raw-data.model';
import { interval, Subscription } from 'rxjs';
import { RawDataService } from 'app/entities/raw-data';
import { JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MapService } from '../map.service';
import { debounceTime, distinctUntilChanged, filter, takeWhile, tap } from 'rxjs/internal/operators';
import { EventThreadResultSet, MapState } from '../../shared/util/map-utils';
import { SideInterface } from '../../shared/side/side.abstract';
import { FormControl } from '@angular/forms';

@Component({
    selector: 'ins-event-thread',
    templateUrl: './event-thread.component.html',
    styles: []
})
export class EventThreadComponent extends SideInterface implements OnInit, OnDestroy {
    currentAccount: any;
    rawDataList: EventThreadResultSet = new EventThreadResultSet([], []);

    itemsPerPage: any;
    page: any;
    predicate: any;
    reverse: true;

    firstId: string;
    lastId: string;
    numberOfItemPerDomPage = 10;

    firstIndex: number;
    lastIndex: number;

    mapStates: MapState;
    mapStatesSubs: Subscription;

    selectedFeatSubs: Subscription;
    selectedFeatureId: string[] = [];

    searchForm: FormControl = new FormControl('');
    currentSearch = '';

    autoRefreshSubs: Subscription;
    canRefresh = false;
    isDestroyed = false; // isDestroyed nécessaire pour arrêter l'interval

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
        super();
        this.itemsPerPage = 20;
        this.predicate = 'rawDataCreationDate';
        this.page = 1;
    }

    updateIdStore(isFirst: boolean, isLast: boolean, id: string): boolean {
        this.firstId = isFirst ? id : this.firstId;
        this.lastId = isLast ? id : this.lastId;
        this.canRefresh = this.firstId === this.rawDataList.dataIds[0];
        return true;
    }

    loadAll() {
        console.log('loading');
        console.log('param ' + (this.page - 1) + ' ' + this.itemsPerPage + ' ' + this.sort());
        this.rawDataService
            .query({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                filter: this.mapStates.FILTER_TYPE // out of 'all', 'locations', 'images', no filter ( '' ) in options means retrieving all data
            })
            .pipe(
                filter((res: HttpResponse<IRawData[]>) => {
                    if (this.mapStates.AUTO_REFRESH && this.canRefresh && res.body && res.body.length) {
                        return res.body[0].id !== this.rawDataList.dataIds[0];
                    }
                    return true;
                }),
                tap((res: HttpResponse<IRawData[]>) => {
                    if (res.ok) {
                        if (this.mapStates.AUTO_REFRESH && this.canRefresh) {
                            // Dans le cas d'un refresh, clear la liste et les indices uniquement
                            // si le premier élément est différent (voir fonction filter précédente)
                            this.clear();
                        }
                        // Envoi en carto
                        this.ms.getFeaturesFromRawData(res.body);
                    }
                })
            )
            .subscribe(
                (res: HttpResponse<IRawData[]>) => this.paginateRawData(res.body),
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
        this.searchForm.valueChanges
            .pipe(
                debounceTime(500),
                distinctUntilChanged()
            )
            .subscribe((value: any) => {
                this.currentSearch = value;
                this.clear();
                this.sendAction('CLEAR_RAW_DATA_SOURCE');
                this.loadAll();
            });
        this.mapStatesSubs = this.ms.mapStates.subscribe((newState: MapState) => {
            const needReload: boolean = this.mapStates !== null && typeof this.mapStates !== 'undefined';
            this.mapStates = newState;
            if (this.mapStates.AUTO_REFRESH && !this.autoRefreshSubs) {
                this.triggerAutoRefresh();
            }
            if (needReload) {
                this.clear();
                this.loadAll();
            }
        });
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.selectedFeatSubs = this.ms.insideFeatureSelector.subscribe(ids => (this.selectedFeatureId = ids));
    }

    ngOnDestroy() {
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }
        if (this.selectedFeatSubs) {
            this.selectedFeatSubs.unsubscribe();
        }
        if (this.autoRefreshSubs) {
            this.autoRefreshSubs.unsubscribe();
        }
        this.isDestroyed = true;
    }

    triggerAutoRefresh() {
        // emit toutes les 5 secondes
        // unsubscribe si AUTO_REFRESH === true
        // N'accepte pas la séquence si canRefresh === false (l'utilisateur n'est plus au top de la page)
        // Déclenche un loadAll avec la première page des rawData
        this.autoRefreshSubs = interval(10000)
            .pipe(
                takeWhile(i => this.mapStates.AUTO_REFRESH && !this.isDestroyed),
                filter(i => this.canRefresh),
                tap(i => {
                    this.page = 1;
                    this.loadAll();
                })
            )
            .subscribe(
                success => console.log('New refresh received'),
                error => console.log('Error auto refresh'),
                () => {
                    this.autoRefreshSubs.unsubscribe();
                    this.autoRefreshSubs = null;
                }
            );
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

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateRawData(data: IRawData[]) {
        console.log('loaded ' + data.length);
        if (data && data.length > 0) {
            const newDataList: EventThreadResultSet = new EventThreadResultSet(
                this.rawDataList.data.concat(data),
                this.rawDataList.dataIds.concat(data.map(item => item.id))
            );
            this.ms.rawDataStream.next(newDataList);
            this.rawDataList = newDataList;
            if (this.firstId) {
                this.firstIndex = this.rawDataList.dataIds.indexOf(this.firstId);
            }
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    getImageBase64Src(content: string): string {
        return `data:image/png;base64,${content}`;
    }

    getSourceTypeIcon(sourceType: string): string {
        switch (sourceType) {
            case 'TWITTER':
                return 'apple-alt';
            case 'RSS':
                return 'rss-square';
            case 'SYSLOG':
                return 'shield-alt';
            default:
                return 'rss-square';
        }
    }

    getDataTypeIcon(dataType: string): string {
        switch (dataType) {
            case 'CYBER':
                return 'user-secret';
            case 'OSINT':
                return 'wifi';
            default:
                return 'rss-square';
        }
    }

    onNewPage(dir: number) {
        const currentIndex: number = this.rawDataList.dataIds.indexOf(this.firstId);
        const nextId: string = dir > 0 ? this.rawDataList.dataIds[currentIndex + 1] : this.rawDataList.dataIds[currentIndex - 1];
        this.firstIndex = nextId
            ? this.rawDataList.dataIds.length > this.numberOfItemPerDomPage
                ? Math.min(this.rawDataList.dataIds.length - this.numberOfItemPerDomPage, this.rawDataList.dataIds.indexOf(nextId))
                : this.rawDataList.dataIds.indexOf(nextId)
            : currentIndex;
        this.lastIndex = this.firstIndex + this.numberOfItemPerDomPage;

        // Si la taille de la liste est inférieure à la taille d'une page, aucun chargement n'est nécessaire.
        if (this.rawDataList.dataIds.length < this.itemsPerPage) {
            return;
        }

        // Si le dernier id de la liste au dernier élément (affiché dans le DOM), charge la page suivante.
        const lastIdIndex: number = this.rawDataList.dataIds.indexOf(this.lastId);
        if (lastIdIndex === this.rawDataList.dataIds.length - 1) {
            this.loadPage(this.page + 1);
        }
    }

    selectOnMap(itemId: string) {
        this.ms.outsideFeatureSelector.next([itemId]);
    }

    sendAction(action: string) {
        this.ms.actionEmitter.next(action);
    }
}
