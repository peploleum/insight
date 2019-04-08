import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from '../../app.constants';
import { Observable } from 'rxjs/index';
import { createRequestOption } from '../util/request-util';
import { filter, map } from 'rxjs/internal/operators';

@Injectable({
    providedIn: 'root'
})
export class SearchService {
    private _resourceUrl = SERVER_API_URL + 'api/insight-elastic/';

    constructor(private http: HttpClient) {}

    autoComplete(entityType: string, search: string): Observable<string[]> {
        const options = createRequestOption({ query: search });
        const url = '_autocomplete/';
        return this.http
            .get(`${this._resourceUrl}` + url + `${entityType}`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<string[]>) => res.ok),
                map((res: HttpResponse<string[]>) => res.body)
            );
    }

    search(entityType: string, search: string): Observable<string[]> {
        const options = createRequestOption({
            page: -1,
            size: 5,
            query: search,
            sort: ['id,' + 'asc']
        });
        const url = '_search/';
        return this.http
            .get(`${this._resourceUrl}` + url + `${entityType}`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<string[]>) => res.ok),
                map((res: HttpResponse<string[]>) => res.body)
            );
    }
}
