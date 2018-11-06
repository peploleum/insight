import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAttackPattern } from 'app/shared/model/attack-pattern.model';

type EntityResponseType = HttpResponse<IAttackPattern>;
type EntityArrayResponseType = HttpResponse<IAttackPattern[]>;

@Injectable({ providedIn: 'root' })
export class AttackPatternService {
    private resourceUrl = SERVER_API_URL + 'api/attack-patterns';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/attack-patterns';

    constructor(private http: HttpClient) {}

    create(attackPattern: IAttackPattern): Observable<EntityResponseType> {
        return this.http.post<IAttackPattern>(this.resourceUrl, attackPattern, { observe: 'response' });
    }

    update(attackPattern: IAttackPattern): Observable<EntityResponseType> {
        return this.http.put<IAttackPattern>(this.resourceUrl, attackPattern, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAttackPattern>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAttackPattern[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAttackPattern[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
