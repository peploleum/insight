/**
 * Created by gFolgoas on 20/02/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { EventThreadResultSet } from '../shared/util/map-utils';
import { FormControl } from '@angular/forms';
import { interval, Subscription } from 'rxjs/index';
import { RawDataService } from '../entities/raw-data/raw-data.service';
import { JhiAlertService } from 'ng-jhipster';
import { debounceTime, distinctUntilChanged, filter, map, takeWhile, tap } from 'rxjs/internal/operators';
import { IRawData } from '../shared/model/raw-data.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ToolbarButtonParameters } from '../shared/util/insight-util';
import { SideMediatorService } from './side-mediator.service';
import { EventThreadParameters, SideAction, SideParameters } from '../shared/util/side.util';

@Component({
    selector: 'ins-side-event-thread',
    templateUrl: 'side-event-thread.component.html'
})
export class SideEventThreadComponent implements OnInit, OnDestroy, AfterViewInit {
    AUTO_REFRESH = false;
    FILTER = 'all';
    SELECTED_DATA_IDS: string[] = [];
    TOOLBAR_ACTIONS: ToolbarButtonParameters[] = [];

    toolbarActionsSubs: Subscription;
    sideParametersSubs: Subscription;
    sideActionSubs: Subscription;
    autoRefreshSubs: Subscription;
    selectedDataSubs: Subscription;

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

    searchForm: FormControl = new FormControl('');
    currentSearch = '';

    canRefresh = false;
    isDestroyed = false; // isDestroyed nécessaire pour arrêter l'interval

    constructor(protected rawDataService: RawDataService, protected jhiAlertService: JhiAlertService, protected sms: SideMediatorService) {
        this.itemsPerPage = 20;
        this.predicate = 'rawDataCreationDate';
        this.page = 1;
    }

    ngOnInit() {
        this.searchForm.valueChanges
            .pipe(
                debounceTime(500),
                distinctUntilChanged()
            )
            .subscribe((value: any) => {
                this.currentSearch = value;
                this.sms._onNewSearchValue.next(value);
                this.clear();
                this.loadAll();
            });
        this.loadAll();
    }

    ngAfterViewInit() {
        this.sideActionSubs = this.sms._sideAction
            .pipe(map((actions: SideAction[]) => actions.filter((a: SideAction) => a.componentTarget === 'EVENT_THREAD')))
            .subscribe((actions: SideAction[]) => {
                actions.forEach(action => {
                    switch (action.action) {
                        case 'LOAD_ALL':
                            this.loadAll();
                            break;
                        case 'CLEAR':
                            this.clear();
                            break;
                        case 'TRIGGER_AUTO_REFRESH':
                            this.triggerAutoRefresh();
                            break;
                        default:
                            break;
                    }
                });
            });
        this.sideParametersSubs = this.sms._sideParameters
            .pipe(
                filter((params: SideParameters<any>[]) => {
                    return params && params.length > 0;
                }),
                map((params: SideParameters<any>[]) => {
                    return params.filter(param => param.parameters instanceof EventThreadParameters);
                })
            )
            .subscribe((params: SideParameters<EventThreadParameters>[]) => {
                params.forEach(param => {
                    this.AUTO_REFRESH = param.parameters.autoRefresh;
                    this.FILTER = param.parameters.filter;
                });
            });
        // setTimeout pour éviter les changeAfterChecked error sur TOOLBAR_ACTIONS à l'initialisation
        setTimeout(() => {
            this.toolbarActionsSubs = this.sms._toolbarActions.subscribe((actions: ToolbarButtonParameters[]) => {
                this.TOOLBAR_ACTIONS = actions;
            });
        });
        this.selectedDataSubs = this.sms._selectedData.subscribe((ids: string[]) => {
            this.SELECTED_DATA_IDS = ids;
        });
    }

    ngOnDestroy() {
        if (this.sideParametersSubs) {
            this.sideParametersSubs.unsubscribe();
        }
        if (this.sideActionSubs) {
            this.sideActionSubs.unsubscribe();
        }
        if (this.toolbarActionsSubs) {
            this.toolbarActionsSubs.unsubscribe();
        }
        if (this.autoRefreshSubs) {
            this.autoRefreshSubs.unsubscribe();
        }
        if (this.selectedDataSubs) {
            this.selectedDataSubs.unsubscribe();
        }
        this.isDestroyed = true;
    }

    updateIdStore(isFirst: boolean, isLast: boolean, id: string): boolean {
        this.firstId = isFirst ? id : this.firstId;
        this.lastId = isLast ? id : this.lastId;
        this.canRefresh = this.firstId === this.rawDataList.dataIds[0];
        return true;
    }

    loadAll() {
        this.rawDataService
            .query({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort(),
                filter: this.FILTER
            })
            .pipe(
                filter((res: HttpResponse<IRawData[]>) => {
                    if (this.AUTO_REFRESH && this.canRefresh && res.body && res.body.length) {
                        return res.body[0].id !== this.rawDataList.dataIds[0];
                    }
                    return true;
                }),
                tap((res: HttpResponse<IRawData[]>) => {
                    if (res.ok) {
                        if (this.AUTO_REFRESH && this.canRefresh) {
                            // Dans le cas d'un refresh, clear la liste et les indices uniquement
                            // si le premier élément est différent (voir fonction filter précédente)
                            this.clear();
                        }
                        this.sms._onNewDataReceived.next(res.body);
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

    triggerAutoRefresh() {
        if (this.AUTO_REFRESH && !this.autoRefreshSubs) {
            /** emit toutes les 5 secondes
             unsubscribe si autoRefreshActivated === false
             N'accepte pas la séquence si canRefresh === false (l'utilisateur n'est plus au top de la page)
             Déclenche un loadAll avec la première page des rawData */
            this.autoRefreshSubs = interval(10000)
                .pipe(
                    takeWhile(i => this.AUTO_REFRESH && !this.isDestroyed),
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
    }

    trackId(index: number, item: IRawData) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateRawData(data: IRawData[]) {
        if (data && data.length > 0) {
            const newDataList: EventThreadResultSet = new EventThreadResultSet(
                this.rawDataList.data.concat(data),
                this.rawDataList.dataIds.concat(data.map(item => item.id))
            );
            this.sms._currentResultSet.next(newDataList);
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

    sendAction(action: string): void {
        this.sms._onNewActionClicked.next(action);
    }

    onRawDataClicked(id: string) {
        this.sms._onNewDataSelected.next([id]);
    }
}
