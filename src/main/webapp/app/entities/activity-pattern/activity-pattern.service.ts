import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IActivityPattern } from 'app/shared/model/activity-pattern.model';

type EntityResponseType = HttpResponse<IActivityPattern>;
type EntityArrayResponseType = HttpResponse<IActivityPattern[]>;

@Injectable({ providedIn: 'root' })
export class ActivityPatternService {
    private resourceUrl = SERVER_API_URL + 'api/activity-patterns';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/activity-patterns';

    constructor(private http: HttpClient) {}

    create(activityPattern: IActivityPattern): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(activityPattern);
        return this.http
            .post<IActivityPattern>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(activityPattern: IActivityPattern): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(activityPattern);
        return this.http
            .put<IActivityPattern>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IActivityPattern>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IActivityPattern[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IActivityPattern[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(activityPattern: IActivityPattern): IActivityPattern {
        const copy: IActivityPattern = Object.assign({}, activityPattern, {
            valideAPartirDe:
                activityPattern.valideAPartirDe != null && activityPattern.valideAPartirDe.isValid()
                    ? activityPattern.valideAPartirDe.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.valideAPartirDe = res.body.valideAPartirDe != null ? moment(res.body.valideAPartirDe) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((activityPattern: IActivityPattern) => {
            activityPattern.valideAPartirDe = activityPattern.valideAPartirDe != null ? moment(activityPattern.valideAPartirDe) : null;
        });
        return res;
    }
}
