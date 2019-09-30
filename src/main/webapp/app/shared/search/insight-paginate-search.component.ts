import { Component, EventEmitter, HostListener, Injector, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
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
    resultParam: EventEmitter<{ totalItems: number }> = new EventEmitter();

    @Input()
    queryParam: { page: number; query: string; size: number } = { page: 0, query: '', size: 10 };
    service: any;
    currentSearch: string;
    searchForm: FormControl = new FormControl('');

    @HostListener('document:keydown.enter', ['$event'])
    onKeydownHandler(event: KeyboardEvent) {
        this.queryParam.query = this.currentSearch;
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
        this.activatedRoute.data.subscribe(data => {
            this.queryParam.page = data.pagingParams.page;
        });
        this.queryParam.query =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
        this.currentSearch = this.queryParam.query;
    }

    ngOnChanges(changes: any) {}

    search(): Observable<HttpResponse<GenericModel[]>> {
        if (!this.queryParam) {
            return;
        }
        return this.service
            .query(this.queryParam)
            .subscribe(
                (res: HttpResponse<IBiographics[]>) => this.paginate(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    navigate() {
        if (!this.queryParam) {
            return;
        }
        this.router.navigate([`${this.urlBase}`, this.queryParam]);
        this.search();
    }

    protected paginate(data: GenericModel[], headers: HttpHeaders) {
        const totalItems = parseInt(headers.get('X-Total-Count'), 10);
        this.resultParam.emit({ totalItems });
        this.resultEmitter.emit(data);
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
