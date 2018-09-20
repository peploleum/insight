import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEquipment } from 'app/shared/model/equipment.model';

type EntityResponseType = HttpResponse<IEquipment>;
type EntityArrayResponseType = HttpResponse<IEquipment[]>;

@Injectable({ providedIn: 'root' })
export class EquipmentService {
    private resourceUrl = SERVER_API_URL + 'api/equipment';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/equipment';

    constructor(private http: HttpClient) {}

    create(equipment: IEquipment): Observable<EntityResponseType> {
        return this.http.post<IEquipment>(this.resourceUrl, equipment, { observe: 'response' });
    }

    update(equipment: IEquipment): Observable<EntityResponseType> {
        return this.http.put<IEquipment>(this.resourceUrl, equipment, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEquipment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEquipment[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEquipment[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
