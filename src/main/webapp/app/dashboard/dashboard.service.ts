import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import {
    EntityMappingInfo,
    IEntityMappingInfo,
    IKibanaDashboardGenerationParameters,
    KibanaDashboardReference
} from './kibana-object.model';
import { DomSanitizer } from '@angular/platform-browser';
import { AnalysisState } from '../shared/model/analysis.model';

type EntityResponseType = HttpResponse<KibanaDashboardReference[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
    private resourceUrl = SERVER_API_URL + '/api/insight-kibana';
    private _getDashboardId = '/get-dashboard-ids';
    private _regenerateDashboard = '/regenerate-dashboard';
    private _deleteAllDashboard = '/delete-all-dashboard';
    private _deleteSingleDashboard = '/delete-single-kibana-object';
    private _postDashboard = '/post-dashboard';
    private _getEntitiesSchema = '/get-entities-schema';

    kibanaUrl: string;

    dashboards: BehaviorSubject<KibanaDashboardReference[]> = new BehaviorSubject([]);
    mappingInfos: BehaviorSubject<EntityMappingInfo[]> = new BehaviorSubject([]);
    analysisState: BehaviorSubject<AnalysisState> = new BehaviorSubject(new AnalysisState(false));

    constructor(private http: HttpClient, private ds: DomSanitizer) {
        if (DEBUG_INFO_ENABLED) {
            this.kibanaUrl = 'http://' + window.location.hostname + ':5601/';
        } else {
            this.kibanaUrl = 'http://192.168.0.120:30100/';
        }
    }

    getEntitiesSchema(): Observable<HttpResponse<IEntityMappingInfo[]>> {
        return this.http.get<IEntityMappingInfo[]>(`${this.resourceUrl + this._getEntitiesSchema}`, { observe: 'response' });
    }

    getDashboardRefs(): Observable<EntityResponseType> {
        return this.http.get<KibanaDashboardReference[]>(`${this.resourceUrl + this._getDashboardId}`, { observe: 'response' });
    }

    regenerateDashboard(): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl + this._regenerateDashboard}`, { observe: 'response' });
    }

    deleteAllDashboard(): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl + this._deleteAllDashboard}`, { observe: 'response' });
    }

    deleteSingleDashboard(id: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl + this._deleteSingleDashboard}/${id}`, { observe: 'response' });
    }

    /**
     * @Param: IKibanaDashboardGenerationParameters paramètres de construction
     * @Return Observable<EntityResponseType> contenant String[], l'id du dashboard créé
     * */
    postDashboard(dashboardParam: IKibanaDashboardGenerationParameters): Observable<EntityResponseType> {
        return this.http.post<KibanaDashboardReference[]>(this.resourceUrl + this._postDashboard, dashboardParam, { observe: 'response' });
    }

    onNewDashboardIdReceived(kdr: KibanaDashboardReference[]) {
        // EntityResponseType avec un dto comportant une fonction ne permet pas de mapper correctement
        // Obliger de recréer les KibanaDashboardReference
        const newRefs: KibanaDashboardReference[] = kdr.map(
            (obj: KibanaDashboardReference) => new KibanaDashboardReference(obj.idObject, obj.title)
        );
        newRefs.forEach(dbRef => {
            dbRef.dashboardSafeUrl = dbRef.getSafeResourceUrl(this.ds, this.kibanaUrl);
            return dbRef;
        });
        if (newRefs && newRefs.length) {
            newRefs[0].selected = true;
        }
        this.dashboards.next(newRefs);
    }
}
