/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import { BehaviorSubject, forkJoin, Observable, of, Subject, throwError } from 'rxjs';
import { IdType } from 'vis';
import { catchError, filter, map, switchMap } from 'rxjs/internal/operators';
import { EdgeDTO, GraphDataCollection, IGraphyNodeDTO, IGraphyRelationDTO, NodeDTO } from 'app/shared/model/node.model';
import { RawDataService } from 'app/entities/raw-data';
import { IRawData, RawData } from 'app/shared/model/raw-data.model';
import { DataContentInfo, NetworkState } from '../shared/util/network.util';
import { ToolbarButtonParameters, UUID } from '../shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class NetworkService {
    private _resourceUrl = SERVER_API_URL + 'api/graph/';
    JSONFileSelected: Subject<File> = new Subject();
    networkState: BehaviorSubject<NetworkState> = new BehaviorSubject({
        LAYOUT_DIR: 'UD',
        LAYOUT_FREE: false,
        PHYSICS_ENABLED: false,
        SORT_METHOD: 'hubsize',
        ADD_NEIGHBOURS: false,
        CLUSTER_NODES: false,
        AUTO_REFRESH: false,
        DISPLAY_RELATION: true
    });

    /** ids des objets contenus dans le network */
    networkDataContent: BehaviorSubject<DataContentInfo[]> = new BehaviorSubject([]);
    /** source de la modal de visualisation de neighbors */
    neighborsEmitter: BehaviorSubject<GraphDataCollection> = new BehaviorSubject(new GraphDataCollection([], []));

    public static getNodeDto(
        label: string,
        objectType?: string,
        id?: IdType,
        mongoId?: string,
        title?: string,
        imageUrl?: string
    ): NodeDTO {
        const image: string = imageUrl ? imageUrl : NetworkService.getNodeImageUrl(objectType);
        const color = {
            border: NetworkService.getNodeColor(objectType)
        };
        const font = {
            color: '#0056b3'
        };
        return new NodeDTO(label, id, mongoId, objectType, title, image, color, 3, font);
    }

    public static getEdgeCollection(idOrigin: IdType, relations: IGraphyRelationDTO[]): EdgeDTO[] {
        return relations.map(rel => NetworkService.getEdgeDto(idOrigin, rel.id));
    }

    public static getDirectNeighboursEdgeCollection(idOrigin: IdType, nodes: NodeDTO[]): EdgeDTO[] {
        return nodes.map(node => NetworkService.getEdgeDto(idOrigin, node.id));
    }

    public static getEdgeDto(from: IdType, to: IdType): EdgeDTO {
        return new EdgeDTO(from, to);
    }

    static getNodeColor(objectType: string): string {
        let color: string;
        switch (objectType) {
            case 'Biographics':
                color = 'red';
                break;
            case 'Event':
                color = 'blue';
                break;
            case 'Equipment':
                color = 'green';
                break;
            case 'RawData':
                color = 'black';
                break;
            default:
                color = 'pink';
                break;
        }
        return color;
    }

    static getNodeImageUrl(objectType: string): string {
        switch (objectType) {
            case 'Biographics':
                return IMAGE_URL_BIO;
                break;
            case 'Event':
                return IMAGE_URL_EVENT;
                break;
            case 'Equipment':
                return IMAGE_URL_EQUIP;
                break;
            case 'RawData':
                return IMAGE_URL_RAW;
                break;
            default:
                return IMAGE_URL_DEFAULT;
        }
    }

    constructor(private http: HttpClient, private rds: RawDataService) {}

    updateRawData(rawDataId: string, symbole: string): Observable<HttpResponse<IRawData>> {
        return this.rds.find(rawDataId).pipe(
            switchMap((res: HttpResponse<IRawData>) => {
                const data: IRawData = res.body;
                data.rawDataSymbol = symbole;
                return this.rds.update(data);
            })
        );
    }

    getMockGraphData(): Observable<GraphDataCollection> {
        return of(MOCK_GRAPH_DATA).pipe(
            map(data => {
                const dataCollection = new GraphDataCollection([], []);
                dataCollection.nodes = data['nodes'].map(item =>
                    NetworkService.getNodeDto(item['label'], item['type'], <IdType>item['id'], item['mongoId'], item['title'])
                );
                dataCollection.edges = data['edges'].map(item => NetworkService.getEdgeDto(<IdType>item['from'], <IdType>item['to']));
                return dataCollection;
            })
        );
    }

    getGraphData(janusIdOrigin: IdType): Observable<GraphDataCollection> {
        const headers = new HttpHeaders();
        const url = 'traversal/';
        return this.http.get(`${this._resourceUrl}` + url + `${janusIdOrigin}`, { headers, observe: 'response' }).pipe(
            catchError(error => {
                if (DEBUG_INFO_ENABLED) {
                    const fakeResponse: HttpResponse<IGraphyNodeDTO[]> = new HttpResponse({
                        body: this.getRamdomizedMockData().nodes,
                        headers: new HttpHeaders(),
                        status: 200
                    });
                    return of(fakeResponse);
                } else {
                    return throwError(error);
                }
            }),
            map((res: HttpResponse<IGraphyNodeDTO[]>) => {
                const body: IGraphyNodeDTO[] = res.body;
                const data = new GraphDataCollection([], []);
                data.nodes = body.map((item: IGraphyNodeDTO) =>
                    NetworkService.getNodeDto(item.label, item.type, item.id, item.mongoId, '', item.image)
                );
                /** Ajoute directement au voisin du Node Origin pour le moment (utiliser getEdgeCollection
                 *  lorsque les relations seront ajoutées au IGraphyNodeDTO depuis le serveur */
                data.edges = NetworkService.getDirectNeighboursEdgeCollection(janusIdOrigin, data.nodes);
                return data;
            })
        );
    }

    getRawDataById(idOrigin: string): Observable<RawData> {
        return this.rds.find(<string>idOrigin).pipe(
            filter((response: HttpResponse<RawData>) => response.ok),
            map((rawData: HttpResponse<RawData>) => rawData.body)
        );
    }

    getNodeProperties(janusIdOrigin: IdType): Observable<HttpResponse<IGraphyNodeDTO>> {
        const headers = new HttpHeaders();
        const url = 'traversal/janus/';
        return this.http
            .get<IGraphyNodeDTO>(`${this._resourceUrl}` + url + `${janusIdOrigin}`, {
                headers,
                observe: 'response'
            })
            .pipe(
                catchError(error => {
                    if (DEBUG_INFO_ENABLED) {
                        const fakeResponse: HttpResponse<IGraphyNodeDTO> = new HttpResponse({
                            body: this.getRamdomizedMockData().nodes[0],
                            headers: new HttpHeaders(),
                            status: 200
                        });
                        return of(fakeResponse);
                    } else {
                        return throwError(error);
                    }
                })
            );
    }

    getUpdatedEventThreadToolbar(): ToolbarButtonParameters[] {
        const newToolbar = [];
        newToolbar.push(
            new ToolbarButtonParameters(
                'AUTO_REFRESH',
                'sync-alt',
                'Activer le rafraîchissement automatique',
                this.networkState.getValue().AUTO_REFRESH
            )
        );
        return newToolbar;
    }

    /**
     * Get MockData with random Id
     * */
    getRamdomizedMockData(): { nodes: any[]; edges: any[] } {
        const mock = MOCK_GRAPH_DATA;
        const random = { nodes: [], edges: [] };
        mock.nodes.forEach((node: { id: IdType; label: string; title: string; type: string }) => {
            const newId = <IdType>UUID();
            const filterE = mock.edges
                .filter((edge: { from: IdType; to: IdType }) => edge.from === node.id || edge.to === node.id)
                .map((edge: { from: IdType; to: IdType }) => {
                    edge.from = edge.from === node.id ? newId : edge.from;
                    edge.to = edge.to === node.id ? newId : edge.to;
                });
            random.edges.concat(filterE);
            node.id = newId;
            random.nodes.push(node);
        });
        return random;
    }
}

export const IMAGE_URL_BIO = '../../../content/images/biographics.png';
export const IMAGE_URL_EVENT = '../../../content/images/event.png';
export const IMAGE_URL_EQUIP = '../../../content/images/equipement.png';
export const IMAGE_URL_DEFAULT = '../../../content/images/default.png';
// export const IMAGE_URL_RAW = '../../../content/images/rawdata.svg';
export const IMAGE_URL_RAW = '../../../content/images/geo-marker.svg';
// export const IMAGE_URL_SELECTED_RAW = '../../../content/images/rawdata_selected.svg';
export const IMAGE_URL_SELECTED_RAW = '../../../content/images/geo-marker-selected.svg';
export const IMAGE_URL_GEOMARKER = '../../../content/images/geo-marker.svg';
export const IMAGE_URL_SELECTED_GEOMARKER = '../../../content/images/geo-marker-selected.svg';
export const MOCK_GRAPH_DATA = {
    nodes: [
        { id: 1, mongoId: '5c90f99b5e365c06ac7e187b', label: 'Bobby', title: 'Personne', type: 'Biographics' },
        { id: 2, mongoId: '2a', label: 'Explosion', title: 'Evenement', type: 'Event' },
        { id: 3, mongoId: '3a', label: 'Voiture', title: 'Equipement', type: 'Equipment' },
        { id: 4, mongoId: '5c90fa1b5e365c06ac7e187c', label: 'Brian', title: 'Personne', type: 'Biographics' },
        { id: 5, mongoId: '5a', label: 'AK-47', title: 'Equipement', type: 'Equipment' },
        { id: 6, mongoId: '5caf3cc4b0b4d23fc041e953', label: 'Attentat', title: 'Evenement', type: 'Event' },
        { id: 7, mongoId: '5c911d434a45de0e0ca2556b', label: 'Raoul', title: 'Personne', type: 'Biographics' },
        { id: 8, mongoId: '5cab0fef4f5f0c0688f6c4a8', label: 'RawData', title: 'Donnee brute', type: 'RawData' },
        { id: 9, mongoId: '5caf3ca6b0b4d23fc041e952', label: 'Rencontre', title: 'Evenement', type: 'Event' },
        { id: 10, mongoId: '5caf3c86b0b4d23fc041e951', label: 'RawData', title: 'Donnee brute', type: 'RawData' }
    ],
    edges: [
        { from: 1, to: 3 },
        { from: 1, to: 2 },
        { from: 1, to: 4 },
        { from: 1, to: 5 },
        { from: 3, to: 5 },
        { from: 3, to: 6 },
        { from: 3, to: 7 },
        { from: 6, to: 10 },
        { from: 2, to: 9 },
        { from: 7, to: 8 },
        { from: 7, to: 8 },
        { from: 9, to: 10 }
    ]
};
