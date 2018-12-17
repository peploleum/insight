import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEvent } from 'app/shared/model/event.model';

type EntityResponseType = HttpResponse<IEvent>;
type EntityArrayResponseType = HttpResponse<IEvent[]>;

@Injectable({ providedIn: 'root' })
export class EventService {
    public resourceUrl = SERVER_API_URL + 'api/events';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/events';

    constructor(protected http: HttpClient) {}

    create(event: IEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(event);
        return this.http
            .post<IEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(event: IEvent): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(event);
        return this.http
            .put<IEvent>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http
            .get<IEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEvent[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(event: IEvent): IEvent {
        const copy: IEvent = Object.assign({}, event, {
            eventDate: event.eventDate != null && event.eventDate.isValid() ? event.eventDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.eventDate = res.body.eventDate != null ? moment(res.body.eventDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((event: IEvent) => {
                event.eventDate = event.eventDate != null ? moment(event.eventDate) : null;
            });
        }
        return res;
    }
}
