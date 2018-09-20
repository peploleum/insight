import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { IKibanaDashboardGenerationParameters, IEntityMappingInfo } from './kibana-object.model';

type EntityResponseType = HttpResponse<string[]>;

@Injectable({ providedIn: 'root' })
export class VisualisationService {
    private resourceUrl = SERVER_API_URL + '/api/elastic-client-resource';
    private getDashboardId = '/get-dashboard';
    private _regenerateDashboard = '/regenerate-dashboard';
    private _postDashboard = '/post-dashboard';
    private _getEntitiesSchema = '/get-entities-schema';

    constructor(private http: HttpClient) {}

    getEntitiesSchema(): Observable<HttpResponse<IEntityMappingInfo[]>> {
        return this.http.get<IEntityMappingInfo[]>(`${this.resourceUrl + this._getEntitiesSchema}`, { observe: 'response' });
    }

    getDefaultDashboardId(): Observable<EntityResponseType> {
        return this.http.get<string[]>(`${this.resourceUrl + this.getDashboardId}`, { observe: 'response' });
    }

    regenerateDashboard(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl + this._regenerateDashboard}`, { observe: 'response' });
    }

    /**
     * @Param: IKibanaDashboardGenerationParameters paramètres de construction
     * @Return Observable<EntityResponseType> contenant String[], l'id du dashboard créé
     * */
    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters): Observable<EntityResponseType> {
        return this.http.post<string[]>(this.resourceUrl + this._postDashboard, dashboardParam, { observe: 'response' });
    }
}
