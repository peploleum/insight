import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IBiographics } from 'app/shared/model/biographics.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { AnalyticsService } from './analytics.service';

import { BiographicsService } from '../entities/biographics/biographics.service';
import { IRawData } from '../shared/model/raw-data.model';
import { addNodes } from '../shared/util/network.util';
import { Edge, IdType } from 'vis';
import { GraphDataCollection, NodeDTO } from '../shared/model/node.model';
import { catchError } from 'rxjs/internal/operators';

@Component({
    selector: 'ins-analytics',
    templateUrl: './analytics.component.html',
    styles: [':host { width:100% }']
})
export class AnalyticsComponent implements OnInit, OnDestroy {
    currentAccount: any;
    biographics: IBiographics[];
    rawData: IRawData[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        protected biographicsService: BiographicsService,
        protected analyticsService: AnalyticsService,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute,
        protected dataUtils: JhiDataUtils,
        protected router: Router,
        protected eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.analyticsService
                .search({
                    page: this.page - 1,
                    query: this.currentSearch,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IBiographics[]>) => this.paginateBiographics(res.body, res.headers),

                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.analyticsService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IBiographics[]>) => this.paginateBiographics(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        // récup liste des bios une fois remplie, pour chaque bio -> requete rawdata-url

        // this.analyticsService.getGraphData()
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/biographics'], {
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
            '/analytics',
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
            '/analytics',
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
        this.registerChangeInAnalytics();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBiographics) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInAnalytics() {
        this.eventSubscriber = this.eventManager.subscribe('analyticsListModification', response => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected paginateRawData(data: IRawData[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.rawData = data;
    }

    protected paginateBiographics(data: IBiographics[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.queryCount = this.totalItems;
        this.biographics = data;
        this.generateTESSCO(data);
    }

    generateTESSCO(data: IBiographics[]) {
        for (let i = 0; i < data.length; i++) {
            var externalId = data[i].externalId;
            console.log('externalId des bio : ' + externalId);
            this.analyticsService.getGraphData(externalId).subscribe(
                (data: GraphDataCollection) => {
                    this.addNodes(data.nodes, data.edges);
                },
                error => {
                    console.log('[NETWORK] Error lors de la récupération des voisins.');
                }
            );
        }
    }

    addNodes(nodes: NodeDTO[], edges: Edge[]) {
        console.log(nodes);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    // addNodes(nodes: Node[], edges: Edge[]) {
    //     // this.network.storePositions();
    //     // addNodes(this.networkData, nodes, edges);
    //     // this.clusterNodes();
    //     console.log("hello");
    // }

    // getNodesNeighbours(idOrigins: IdType[]) {
    //     for (const i of idOrigins) {
    //         this.analyticsService.getGraphData(i).subscribe(
    //             (data: GraphDataCollection) => {
    //                 this.addNodes(data.nodes, data.edges);
    //             },
    //             error => {
    //                 console.log('[NETWORK] Error lors de la récupération des voisins.');
    //             }
    //         );
    //     }
    // }
}
