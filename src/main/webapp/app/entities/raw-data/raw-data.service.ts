import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRawData } from 'app/shared/model/raw-data.model';

type EntityResponseType = HttpResponse<IRawData>;
type EntityArrayResponseType = HttpResponse<IRawData[]>;

@Injectable({ providedIn: 'root' })
export class RawDataService {
    public resourceUrl = SERVER_API_URL + 'api/raw-data';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/raw-data';

    constructor(protected http: HttpClient) {}

    create(rawData: IRawData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rawData);
        return this.http
            .post<IRawData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(rawData: IRawData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(rawData);
        return this.http
            .put<IRawData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IRawData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRawData[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRawData[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(rawData: IRawData): IRawData {
        const copy: IRawData = Object.assign({}, rawData, {
            rawDataCreationDate:
                rawData.rawDataCreationDate != null && rawData.rawDataCreationDate.isValid() ? rawData.rawDataCreationDate.toJSON() : null,
            rawDataExtractedDate:
                rawData.rawDataExtractedDate != null && rawData.rawDataExtractedDate.isValid()
                    ? rawData.rawDataExtractedDate.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.rawDataCreationDate = res.body.rawDataCreationDate != null ? moment(res.body.rawDataCreationDate) : null;
            res.body.rawDataExtractedDate = res.body.rawDataExtractedDate != null ? moment(res.body.rawDataExtractedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((rawData: IRawData) => {
                rawData.rawDataCreationDate = rawData.rawDataCreationDate != null ? moment(rawData.rawDataCreationDate) : null;
                rawData.rawDataExtractedDate = rawData.rawDataExtractedDate != null ? moment(rawData.rawDataExtractedDate) : null;
            });
        }
        return res;
    }
}
