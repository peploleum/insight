import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBiographics } from 'app/shared/model/biographics.model';
import {
    convertResponseEntitiesDateArrayFromServer,
    convertEntitiesDateFromClient,
    convertResponseEntitiesDateFromServer
} from 'app/shared/util/insight-util';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IBiographics>;
type EntityArrayResponseType = HttpResponse<IBiographics[]>;

@Injectable({ providedIn: 'root' })
export class BiographicsService {
    public resourceUrl = SERVER_API_URL + 'api/biographics';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/biographics';

    constructor(protected http: HttpClient) {}

    create(biographics: IBiographics): Observable<EntityResponseType> {
        const copy = convertEntitiesDateFromClient(biographics, ['biographicsCreationDate']);
        return this.http
            .post<IBiographics>(this.resourceUrl, copy as IBiographics, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => convertResponseEntitiesDateFromServer(res, ['biographicsCreationDate'])));
    }

    update(biographics: IBiographics): Observable<EntityResponseType> {
        const copy = convertEntitiesDateFromClient(biographics, ['biographicsCreationDate']);
        return this.http
            .put<IBiographics>(this.resourceUrl, copy as IBiographics, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => convertResponseEntitiesDateFromServer(res, ['biographicsCreationDate'])));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IBiographics>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => convertResponseEntitiesDateFromServer(res, ['biographicsCreationDate'])));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBiographics[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => convertResponseEntitiesDateArrayFromServer(res, ['biographicsCreationDate'])));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBiographics[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => convertResponseEntitiesDateArrayFromServer(res, ['biographicsCreationDate'])));
    }
}
