import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IDictionary } from 'app/shared/model/analytics.model';

@Injectable({ providedIn: 'root' })
export class DictionaryService {
    public resourceUrl = SERVER_API_URL + 'api/dictionary';

    constructor(protected http: HttpClient) {}

    create(dico: IDictionary): Observable<HttpResponse<IDictionary>> {
        return this.http.post<IDictionary>(this.resourceUrl, dico, { observe: 'response' });
    }

    update(dico: IDictionary): Observable<HttpResponse<IDictionary>> {
        return this.http.put<IDictionary>(this.resourceUrl, dico, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<IDictionary>> {
        return this.http.get<IDictionary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
