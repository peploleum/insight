import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { IDictionary } from 'app/shared/model/analytics.model';
import { ILoadedFormFile, IProcessedFormFile } from 'app/shared/model/pipeline.model';
import { DEBUG_INFO_ENABLED, HOUSTON_API_URL, SERVER_API_URL } from 'app/app.constants';
import { map } from 'rxjs/operators';
import { UUID } from 'app/shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class PipelineService {
    public resourceUrl = HOUSTON_API_URL + 'start_process94A';
    public insightUrl = SERVER_API_URL + 'api/graph/traversal';
    public loadedFiles: BehaviorSubject<ILoadedFormFile[]> = new BehaviorSubject([]);
    public processedFiles: BehaviorSubject<IProcessedFormFile[]> = new BehaviorSubject([]);

    constructor(protected http: HttpClient) {}

    sendForm(formContent: string): Observable<HttpResponse<string>> {
        const headers: HttpHeaders = new HttpHeaders();
        // if (DEBUG_INFO_ENABLED) {
        //     return this.fakeSendForm();
        // }
        return this.http.post<string>(this.resourceUrl, formContent, { headers, observe: 'response' });
    }

    update(dico: IDictionary): Observable<HttpResponse<IDictionary>> {
        return this.http.put<IDictionary>(this.resourceUrl, dico, { observe: 'response' });
    }

    find(id: string): Observable<HttpResponse<IDictionary>> {
        return this.http.get<IDictionary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getProcessStatus(file: IProcessedFormFile): Observable<IProcessedFormFile> {
        return this.http
            .post<IProcessedFormFile>(`${this.insightUrl}/status`, file, { observe: 'response' })
            .pipe(map((res: HttpResponse<IProcessedFormFile>) => res.body));
    }

    fakeSendForm(): Observable<HttpResponse<string>> {
        const fakeResponse: HttpResponse<string> = new HttpResponse({
            body: UUID(),
            headers: new HttpHeaders(),
            status: 200
        });
        return of(fakeResponse);
    }
}
