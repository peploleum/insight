import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import { IDictionary } from 'app/shared/model/analytics.model';
import { createRequestOption } from 'app/shared';
import { map } from 'rxjs/operators';

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

    getAll(params?: any): Observable<HttpResponse<IDictionary[]>> {
        if (!params) {
            params = {
                page: 0,
                size: 10,
                sort: ['id,desc']
            };
        }
        const options = createRequestOption(params);
        return this.http.get<IDictionary[]>(`${this.resourceUrl}`, { params: options, observe: 'response' }).pipe(
            map(res => {
                if (res.body && res.body.length === 0 && DEBUG_INFO_ENABLED) {
                    const fakeResponse: HttpResponse<IDictionary[]> = new HttpResponse({
                        body: FAKE_DICO,
                        headers: new HttpHeaders(),
                        status: 200
                    });
                    return fakeResponse;
                } else {
                    return res;
                }
            })
        );
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

const FAKE_DICO: IDictionary[] = [
    {
        id: 'fakeId1',
        name: 'dico de legumes',
        theme: [
            {
                name: 'TER',
                motclef: [
                    { clef: 'papillon', pond: 12 },
                    { clef: 'barbecue', pond: 3 },
                    { clef: 'saucisse', pond: 5 },
                    { clef: 'chou fleur', pond: 2 }
                ]
            },
            {
                name: 'ESP',
                motclef: [
                    { clef: 'betterave', pond: 6 },
                    { clef: 'courgette', pond: 7 },
                    { clef: 'carotte', pond: 20 },
                    { clef: 'tomate', pond: 4 }
                ]
            },
            {
                name: 'SAB',
                motclef: [
                    { clef: 'celeri', pond: 1 },
                    { clef: 'laitue', pond: 9 },
                    { clef: 'potiron', pond: 10 },
                    { clef: 'chose', pond: 2 }
                ]
            },
            {
                name: 'SUB',
                motclef: [{ clef: 'pomme', pond: 3 }, { clef: 'poire', pond: 15 }, { clef: 'raisin', pond: 17 }, { clef: 'truc', pond: 2 }]
            }
        ]
    },
    {
        id: 'fakeId2',
        name: 'dico en bordel',
        theme: [
            {
                name: 'CRO',
                motclef: [
                    { clef: 'nain', pond: 12 },
                    { clef: 'jardin', pond: 3 },
                    { clef: 'fleur', pond: 5 },
                    { clef: 'cuillère', pond: 2 }
                ]
            },
            {
                name: 'TER',
                motclef: [
                    { clef: 'kdsj', pond: 6 },
                    { clef: 'courgette', pond: 7 },
                    { clef: 'carotte', pond: 20 },
                    { clef: 'tomate', pond: 4 }
                ]
            },
            {
                name: 'SAB',
                motclef: [
                    { clef: 'celeri', pond: 1 },
                    { clef: 'laitue', pond: 9 },
                    { clef: 'potiron', pond: 10 },
                    { clef: 'chose', pond: 2 }
                ]
            }
        ]
    }
];
