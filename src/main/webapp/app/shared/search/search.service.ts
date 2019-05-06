import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from '../../app.constants';
import { Observable } from 'rxjs/index';
import { createRequestOption } from '../util/request-util';
import { filter, map } from 'rxjs/internal/operators';
import { GenericModel } from '../model/generic.model';
import Projection from 'ol/proj';
import Extent from 'ol/extent';

@Injectable({
    providedIn: 'root'
})
export class SearchService {
    private _searchIndice = SERVER_API_URL + 'api/insight-elastic/';
    private _searchIndices = SERVER_API_URL + 'api/insight-elastic/_search/indices';
    private _searchGeo = SERVER_API_URL + 'api/insight-elastic/_search/indices/geo';

    private itemsPerPage = 50;
    private sort = ['id', 'asc'];
    private indices = ['rawdata', 'biographics', 'event', 'location', 'equipment', 'organisation'];

    constructor(private http: HttpClient) {}

    autoComplete(entityType: string, search: string): Observable<string[]> {
        const options = createRequestOption({ query: search });
        const url = '_autocomplete/';
        return this.http
            .get(`${this._searchIndice}` + url + `${entityType}`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<string[]>) => res.ok),
                map((res: HttpResponse<string[]>) => res.body)
            );
    }

    searchIndice(entityType: string, search: string): Observable<GenericModel[]> {
        const options = createRequestOption({
            page: -1,
            size: 5,
            query: search,
            sort: ['id,' + 'asc']
        });
        const url = '_search/';
        return this.http
            .get(`${this._searchIndice}` + url + `${entityType}`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<GenericModel[]>) => res.ok),
                map((res: HttpResponse<GenericModel[]>) => res.body)
            );
    }

    searchIndices(search: string, page?: number, size?: number, sort?: any, indices?: string[], extent?: any): Observable<GenericModel[]> {
        const req = {
            query: search,
            page: page || -1,
            size: size || this.itemsPerPage,
            sort: sort || this.sort,
            indices: indices || this.indices,
            envelope: extent ? this.formatExtentCoordinate(extent) : null
        };
        const options = createRequestOption(req);
        return this.http
            .get<GenericModel[]>(`${extent ? this._searchGeo : this._searchIndices}`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<GenericModel[]>) => res.ok),
                map((res: HttpResponse<GenericModel[]>) => res.body)
            );
    }

    /**
     * Transforme un ol.Extent en Array de coordinate compatible ES GeoQuery
     * */
    formatExtentCoordinate(extent: any): number[] {
        const topLeft = Projection.toLonLat(Extent.getTopLeft(extent));
        const bottomRight = Projection.toLonLat(Extent.getBottomRight(extent));
        return [topLeft[0], topLeft[1], bottomRight[0], bottomRight[1]];
    }
}
