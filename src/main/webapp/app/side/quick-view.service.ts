import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { Observable, throwError } from 'rxjs/index';
import { GenericModel } from '../shared/model/generic.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IRawData } from '../shared/model/raw-data.model';
import { convertRawDataDateFromClient, convertRawDataDateFromServer } from '../shared/util/insight-util';
import { map } from 'rxjs/internal/operators';
import { createRequestOption } from '../shared/util/request-util';
import { RawDataService } from '../entities/raw-data/raw-data.service';
import { BiographicsService } from '../entities/biographics/biographics.service';
import { EventService } from '../entities/event/event.service';
import { OrganisationService } from '../entities/organisation/organisation.service';
import { EquipmentService } from '../entities/equipment/equipment.service';
import { LocationService } from '../entities/location/location.service';

type EntityResponseType = HttpResponse<GenericModel>;
type EntitiesResponseType = HttpResponse<GenericModel[]>;

@Injectable({
    providedIn: 'root'
})
export class QuickViewService {
    public resourceUrl = SERVER_API_URL + 'api';

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
        return this.http.get<GenericModel>(`${this.resourceUrl}/entity/${id}`, { observe: 'response' });
    }

    findMultiple(ids: string[]): Observable<EntitiesResponseType> {
        return this.http.post<GenericModel[]>(`${this.resourceUrl}/entities`, ids, { observe: 'response' });
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
            .put<IRawData>(`${this.resourceUrl}/updateAnnotation`, copy, { params: options, observe: 'response' })
            .pipe(map((res: HttpResponse<IRawData>) => convertRawDataDateFromServer(res)));
    }
}
