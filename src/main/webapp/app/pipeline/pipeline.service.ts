import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IDictionary } from 'app/shared/model/analytics.model';
import { ILoadedFormFile, IProcessedFormFile } from 'app/shared/model/pipeline.model';

@Injectable({ providedIn: 'root' })
export class PipelineService {
    public resourceUrl = SERVER_API_URL + 'api/dictionary';
    public loadedFiles: BehaviorSubject<ILoadedFormFile[]> = new BehaviorSubject([]);
    public processedFiles: BehaviorSubject<IProcessedFormFile[]> = new BehaviorSubject([]);

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
}
