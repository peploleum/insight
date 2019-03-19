import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { Observable } from 'rxjs/index';
import { GenericModel } from '../shared/model/quick-view.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IRawData } from '../shared/model/raw-data.model';
import { convertRawDataDateFromClient, convertRawDataDateFromServer } from '../shared/util/insight-util';
import { map } from 'rxjs/internal/operators';
import { createRequestOption } from '../shared/util/request-util';

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

    saveAnnotatedEntity(entityPos: any, rawDataToUpdate: IRawData): Observable<HttpResponse<IRawData>> {
        const options = createRequestOption(entityPos);
        const copy = convertRawDataDateFromClient(rawDataToUpdate);
        return this.http
            .put<IRawData>(`${this.resourceUrl}/updateAnnotation`, copy, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<IRawData>) => convertRawDataDateFromServer(res)));
    }
}
