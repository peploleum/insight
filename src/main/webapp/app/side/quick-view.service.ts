import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { Observable, throwError } from 'rxjs/index';
import { GenericModel } from '../shared/model/generic.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IRawData } from '../shared/model/raw-data.model';
import {
    assertGenericType,
    convertRawDataDateFromClient,
    convertRawDataDateFromServer,
    removeDuplicate
} from '../shared/util/insight-util';
import { map } from 'rxjs/internal/operators';
import { createRequestOption } from '../shared/util/request-util';
import { RawDataService } from '../entities/raw-data/raw-data.service';
import { BiographicsService } from '../entities/biographics/biographics.service';
import { EventService } from '../entities/event/event.service';
import { OrganisationService } from '../entities/organisation/organisation.service';
import { EquipmentService } from '../entities/equipment/equipment.service';
import { LocationService } from '../entities/location/location.service';
import { IGraphStructureNodeDTO } from '../shared/model/node.model';

type EntityResponseType = HttpResponse<GenericModel>;
type EntitiesResponseType = HttpResponse<GenericModel[]>;

@Injectable({
    providedIn: 'root'
})
export class QuickViewService {
    public _qvResourceUrl = SERVER_API_URL + 'api';
    private _graphResourceUrl = SERVER_API_URL + 'api/graph/';

    constructor(
        protected http: HttpClient,
        private _rds: RawDataService,
        private _bs: BiographicsService,
        private _evs: EventService,
        private _os: OrganisationService,
        private _eqs: EquipmentService,
        private _ls: LocationService
    ) {}

    find(id: string): Observable<EntityResponseType> {
        return this.http.get<GenericModel>(`${this._qvResourceUrl}/entity/${id}`, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => {
                if (assertGenericType('RawData', res.body)) {
                    convertRawDataDateFromServer(res.body);
                }
                return res;
            })
        );
    }

    findMultiple(ids: string[]): Observable<EntitiesResponseType> {
        return this.http.post<GenericModel[]>(`${this._qvResourceUrl}/entities`, ids, { observe: 'response' }).pipe(
            map((res: EntitiesResponseType) => {
                (<GenericModel[]>res.body).forEach(model => {
                    if (assertGenericType('RawData', model)) {
                        convertRawDataDateFromServer(model);
                    }
                });
                return res;
            })
        );
    }

    /**
     * entityType de GenericModel doit être spécifié
     * */
    update(entity: GenericModel): Observable<EntityResponseType> {
        let upFct: Observable<any>;
        switch (entity['entityType']) {
            case 'Biographics':
                upFct = this._bs.update(entity);
                break;
            case 'Equipment':
                upFct = this._eqs.update(entity);
                break;
            case 'Event':
                upFct = this._evs.update(entity);
                break;
            case 'Location':
                upFct = this._ls.update(entity);
                break;
            case 'Organisation':
                upFct = this._os.update(entity);
                break;
            case 'RawData':
                upFct = this._rds.update(entity);
                break;
            default:
                break;
        }
        return upFct || throwError(`Type could not be found. Update failed.`);
    }

    saveAnnotatedEntity(entityPos: any, rawDataToUpdate: IRawData): Observable<HttpResponse<IRawData>> {
        const options = createRequestOption(entityPos);
        const copy = convertRawDataDateFromClient(rawDataToUpdate);
        return this.http
            .put<IRawData>(`${this._qvResourceUrl}/updateAnnotation`, copy, {
                params: options,
                observe: 'response'
            })
            .pipe(
                map((res: HttpResponse<IRawData>) => {
                    convertRawDataDateFromServer(res.body);
                    return res;
                })
            );
    }

    getGraphForEntity(externalId: string, lvlOrder: number): Observable<HttpResponse<IGraphStructureNodeDTO>> {
        const options = createRequestOption({ levelOrder: lvlOrder });
        const url = 'traversal/getGraph/';
        return this.http.get<IGraphStructureNodeDTO>(`${this._graphResourceUrl}` + url + `${externalId}`, {
            params: options,
            observe: 'response'
        });
    }

    /**
     * Une requête par niveau de graph, voir à évoluer vers des forkjoins pour des graphs de grande taille
     * */
    resolveGraph(graph: IGraphStructureNodeDTO): Observable<HttpResponse<GenericModel[]>> {
        const rootId: string = graph.nodeId;
        const firstLevelIds: string[] = graph.relations.map(node => node.nodeId);
        const secondLevelIds: string[] = graph.relations
            .filter(node => node.relations && node.relations.length > 0)
            .map(node => node.relations)
            .reduce((x, y) => x.concat(y), [])
            .map(node => node.nodeId);
        const ids: string[] = removeDuplicate([rootId].concat(firstLevelIds).concat(secondLevelIds));
        return this.findMultiple(ids);
    }
}
