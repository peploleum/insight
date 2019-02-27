import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { Observable } from 'rxjs/index';
import { GenericModel } from '../shared/model/quick-view.model';
import { HttpClient, HttpResponse } from '@angular/common/http';

type EntityResponseType = HttpResponse<GenericModel>;

@Injectable({
    providedIn: 'root'
})
export class QuickViewService {
    public resourceUrl = SERVER_API_URL + 'api';

    constructor(protected http: HttpClient) {}

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<GenericModel>(`${this.resourceUrl}/entity/${id}`, { observe: 'response' });
    }
}
