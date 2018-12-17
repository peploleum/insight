import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IOrganisation } from 'app/shared/model/organisation.model';

type EntityResponseType = HttpResponse<IOrganisation>;
type EntityArrayResponseType = HttpResponse<IOrganisation[]>;

@Injectable({ providedIn: 'root' })
export class OrganisationService {
    public resourceUrl = SERVER_API_URL + 'api/organisations';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/organisations';

    constructor(protected http: HttpClient) {}

    create(organisation: IOrganisation): Observable<EntityResponseType> {
        return this.http.post<IOrganisation>(this.resourceUrl, organisation, { observe: 'response' });
    }

    update(organisation: IOrganisation): Observable<EntityResponseType> {
        return this.http.put<IOrganisation>(this.resourceUrl, organisation, { observe: 'response' });
    }

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<IOrganisation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganisation[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IOrganisation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
