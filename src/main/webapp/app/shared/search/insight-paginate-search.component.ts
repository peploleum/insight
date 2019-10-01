import { Component, EventEmitter, HostListener, Injector, Input, OnChanges, OnInit, Output } from '@angular/core';
import { GenericModel } from '../model/generic.model';
import { Observable } from 'rxjs/index';
import { RawDataService } from 'app/entities/raw-data';
import { BiographicsService } from 'app/entities/biographics';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { ActivatedRoute, Router } from '@angular/router';
import { IBiographics } from 'app/shared/model/biographics.model';

@Component({
    selector: 'ins-insight-paginate-search',
    templateUrl: './insight-paginate-search.component.html',
    styleUrls: ['insight-search.scss']
})
export class InsightPaginateSearchComponent implements OnInit, OnChanges {
    @Input()
    targetEntity: 'rawdata' | 'biographics';
    @Input()
    urlBase: string;
    @Output()
    resultEmitter: EventEmitter<any> = new EventEmitter();
    @Output()
    resultParam: EventEmitter<{ totalItems: number; query: string; size: number; page: number }> = new EventEmitter();

    private _queryParam: { page: number; query: string; size: number; sort: string[] } = {
        page: 0,
        query: '*',
        size: 10,
        sort: this.sort()
    };
    service: any;
    currentSearch: string;

    @Input()
    set queryParam(page: number) {
        const needReloading = this._queryParam.page !== page - 1;
        this._queryParam.page = page - 1;
        if (needReloading) {
            this.navigate();
        }
    }

    @HostListener('document:keydown.enter', ['$event'])
    onKeydownHandler(event: KeyboardEvent) {
        this._queryParam.query = this.currentSearch;
        this.navigate();
    }

    constructor(
        private _inj: Injector,
        protected jhiAlertService: JhiAlertService,
        protected activatedRoute: ActivatedRoute,
        protected router: Router
    ) {}

    ngOnInit() {
        switch (this.targetEntity) {
            case 'rawdata':
                this.service = this._inj.get<RawDataService>(RawDataService);
                break;
            case 'biographics':
                this.service = this._inj.get<BiographicsService>(BiographicsService);
                break;
        }
        this.activatedRoute.params.subscribe(params => {
            if (params['query']) {
                this._queryParam.query = params['query'];
                this.currentSearch = this._queryParam.query;
            }
            if (params['page']) {
                this._queryParam.page = parseInt(params['page'], 10);
            }
            if (params['size']) {
                this._queryParam.size = params['size'];
            }
            this.search();
        });
    }

    ngOnChanges(changes: any) {}

    search(): Observable<HttpResponse<GenericModel[]>> {
        if (!this._queryParam) {
            return;
        }
        return this.service
            .search(this._queryParam)
            .subscribe(
                (res: HttpResponse<IBiographics[]>) => this.paginate(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    navigate() {
        if (!this._queryParam) {
            return;
        }
        this.router.navigate([`${this.urlBase}`, this._queryParam]);
    }

    protected paginate(data: GenericModel[], headers: HttpHeaders) {
        const totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.resultParam.emit({
            totalItems,
            query: this._queryParam.query,
            size: this._queryParam.size,
            page: this._queryParam.page
        });
        this.resultEmitter.emit(data);
    }

    protected sort(): string[] {
        const predicate = 'id';
        const reverse = false;
        const result = [predicate + ',' + (reverse ? 'asc' : 'desc')];
        if (predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
