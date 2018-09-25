import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { IKibanaDashboardGenerationParameters, IEntityMappingInfo } from './kibana-object.model';

type EntityResponseType = HttpResponse<string[]>;

@Injectable({ providedIn: 'root' })
export class VisualisationService {
    private resourceUrl = SERVER_API_URL + '/api/elastic-client-resource';
    private _getDashboardId = '/get-dashboard-ids';
    private _regenerateDashboard = '/regenerate-dashboard';
    private _deleteAllDashboard = '/delete-all-dashboard';
    private _postDashboard = '/post-dashboard';
    private _getEntitiesSchema = '/get-entities-schema';
    kibanaUrl = 'http://192.168.99.100:5601/';

    constructor(private http: HttpClient) {}

    getEntitiesSchema(): Observable<HttpResponse<IEntityMappingInfo[]>> {
        return this.http.get<IEntityMappingInfo[]>(`${this.resourceUrl + this._getEntitiesSchema}`, { observe: 'response' });
    }

    getDashboardIds(): Observable<EntityResponseType> {
        return this.http.get<string[]>(`${this.resourceUrl + this._getDashboardId}`, { observe: 'response' });
    }

    regenerateDashboard(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl + this._regenerateDashboard}`, { observe: 'response' });
    }

    deleteAllDashboard(): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl + this._deleteAllDashboard}`, { observe: 'response' });
    }

    /**
     * @Param: IKibanaDashboardGenerationParameters paramètres de construction
     * @Return Observable<EntityResponseType> contenant String[], l'id du dashboard créé
     * */
    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters): Observable<EntityResponseType> {
        return this.http.post<string[]>(this.resourceUrl + this._postDashboard, dashboardParam, { observe: 'response' });
    }
}
