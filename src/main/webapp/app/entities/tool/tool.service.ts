import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITool } from 'app/shared/model/tool.model';

type EntityResponseType = HttpResponse<ITool>;
type EntityArrayResponseType = HttpResponse<ITool[]>;

@Injectable({ providedIn: 'root' })
export class ToolService {
    private resourceUrl = SERVER_API_URL + 'api/tools';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tools';

    constructor(private http: HttpClient) {}

    create(tool: ITool): Observable<EntityResponseType> {
        return this.http.post<ITool>(this.resourceUrl, tool, { observe: 'response' });
    }

    update(tool: ITool): Observable<EntityResponseType> {
        return this.http.put<ITool>(this.resourceUrl, tool, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITool>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITool[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITool[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
