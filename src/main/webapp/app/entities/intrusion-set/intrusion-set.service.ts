import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';

type EntityResponseType = HttpResponse<IIntrusionSet>;
type EntityArrayResponseType = HttpResponse<IIntrusionSet[]>;

@Injectable({ providedIn: 'root' })
export class IntrusionSetService {
    private resourceUrl = SERVER_API_URL + 'api/intrusion-sets';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/intrusion-sets';

    constructor(private http: HttpClient) {}

    create(intrusionSet: IIntrusionSet): Observable<EntityResponseType> {
        return this.http.post<IIntrusionSet>(this.resourceUrl, intrusionSet, { observe: 'response' });
    }

    update(intrusionSet: IIntrusionSet): Observable<EntityResponseType> {
        return this.http.put<IIntrusionSet>(this.resourceUrl, intrusionSet, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IIntrusionSet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIntrusionSet[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IIntrusionSet[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
