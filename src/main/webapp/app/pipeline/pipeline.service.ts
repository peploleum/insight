import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { IDictionary } from 'app/shared/model/analytics.model';
import { ILoadedFormFile, IProcessedFormFile } from 'app/shared/model/pipeline.model';

@Injectable({ providedIn: 'root' })
export class PipelineService {
    public resourceUrl = 'http://192.168.0.9:8090/' + '_process94A';
    public loadedFiles: BehaviorSubject<ILoadedFormFile[]> = new BehaviorSubject([]);
    public processedFiles: BehaviorSubject<IProcessedFormFile[]> = new BehaviorSubject([]);

    constructor(protected http: HttpClient) {}

    sendForm(formContent: string): Observable<HttpResponse<string>> {
        return this.http.post<string>(this.resourceUrl, formContent, { observe: 'response' });
    }

    update(dico: IDictionary): Observable<HttpResponse<IDictionary>> {
        return this.http.put<IDictionary>(this.resourceUrl, dico, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<IDictionary>> {
        return this.http.get<IDictionary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
