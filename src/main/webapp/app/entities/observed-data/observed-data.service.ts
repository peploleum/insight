import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IObservedData } from 'app/shared/model/observed-data.model';

type EntityResponseType = HttpResponse<IObservedData>;
type EntityArrayResponseType = HttpResponse<IObservedData[]>;

@Injectable({ providedIn: 'root' })
export class ObservedDataService {
    private resourceUrl = SERVER_API_URL + 'api/observed-data';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/observed-data';

    constructor(private http: HttpClient) {}

    create(observedData: IObservedData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(observedData);
        return this.http
            .post<IObservedData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(observedData: IObservedData): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(observedData);
        return this.http
            .put<IObservedData>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IObservedData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IObservedData[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IObservedData[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(observedData: IObservedData): IObservedData {
        const copy: IObservedData = Object.assign({}, observedData, {
            dateDebut: observedData.dateDebut != null && observedData.dateDebut.isValid() ? observedData.dateDebut.toJSON() : null,
            dateFin: observedData.dateFin != null && observedData.dateFin.isValid() ? observedData.dateFin.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.dateDebut = res.body.dateDebut != null ? moment(res.body.dateDebut) : null;
        res.body.dateFin = res.body.dateFin != null ? moment(res.body.dateFin) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((observedData: IObservedData) => {
            observedData.dateDebut = observedData.dateDebut != null ? moment(observedData.dateDebut) : null;
            observedData.dateFin = observedData.dateFin != null ? moment(observedData.dateFin) : null;
        });
        return res;
    }
}
