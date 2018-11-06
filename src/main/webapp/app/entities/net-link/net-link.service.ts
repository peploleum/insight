import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { INetLink } from 'app/shared/model/net-link.model';

type EntityResponseType = HttpResponse<INetLink>;
type EntityArrayResponseType = HttpResponse<INetLink[]>;

@Injectable({ providedIn: 'root' })
export class NetLinkService {
    private resourceUrl = SERVER_API_URL + 'api/net-links';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/net-links';

    constructor(private http: HttpClient) {}

    create(netLink: INetLink): Observable<EntityResponseType> {
        return this.http.post<INetLink>(this.resourceUrl, netLink, { observe: 'response' });
    }

    update(netLink: INetLink): Observable<EntityResponseType> {
        return this.http.put<INetLink>(this.resourceUrl, netLink, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<INetLink>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INetLink[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<INetLink[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
