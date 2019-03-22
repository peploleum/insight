/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { BehaviorSubject, Observable, of, Subject, throwError } from 'rxjs';
import { IdType } from 'vis';
import { catchError, filter, map } from 'rxjs/internal/operators';
import { EdgeDTO, GraphDataCollection, IGraphyNodeDTO, IGraphyRelationDTO, NodeDTO } from 'app/shared/model/node.model';
import { RawDataService } from 'app/entities/raw-data';
import { RawData } from 'app/shared/model/raw-data.model';
import { DataContentInfo, NetworkState } from '../shared/util/network.util';
import { ToolbarButtonParameters, UUID } from '../shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class NetworkService {
    private _resourceUrl;
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

    constructor(private http: HttpClient, private rawDataService: RawDataService) {
        if (DEBUG_INFO_ENABLED) {
            this._resourceUrl = 'http://' + window.location.hostname + ':8090/';
        } else {
            this._resourceUrl = 'http://192.168.0.120:30200/';
        }
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
        const url = 'api/traversal/';
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
                data.nodes = body.map((item: IGraphyNodeDTO) => NetworkService.getNodeDto(item.label, item.type, item.id, item.mongoId));
                /** Ajoute directement au voisin du Node Origin pour le moment (utiliser getEdgeCollection
                 *  lorsque les relations seront ajoutées au IGraphyNodeDTO depuis le serveur */
                data.edges = NetworkService.getDirectNeighboursEdgeCollection(janusIdOrigin, data.nodes);
                return data;
            })
        );
    }

    getRawDataById(idOrigin: string): Observable<RawData> {
        return this.rawDataService.find(<string>idOrigin).pipe(
            filter((response: HttpResponse<RawData>) => response.ok),
            map((rawData: HttpResponse<RawData>) => rawData.body)
        );
    }

    getNodeProperties(janusIdOrigin: IdType): Observable<HttpResponse<IGraphyNodeDTO>> {
        const headers = new HttpHeaders();
        // if (DEBUG_INFO_ENABLED) {
        //     /** Tant que l'endpoint de graphy n'est pas dispo */
        //     const fakeResponse: HttpResponse<IGraphyNodeDTO> = new HttpResponse({
        //         body: new GraphyNodeDTO(UUID(), 'Personne Origine', 'Biographics'),
        //         headers: new HttpHeaders(),
        //         status: 200
        //     });
        //     return of(fakeResponse);
        // }
        const url = DEBUG_INFO_ENABLED ? 'api/traversal/janus/' : 'api/traversal/janus/';
        return this.http.get<IGraphyNodeDTO>(`${this._resourceUrl}` + url + `${janusIdOrigin}`, {
            headers,
            observe: 'response'
        });
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

    getUpdatedNetworkContentToolbar(): ToolbarButtonParameters[] {
        const newToolbar = [];
        newToolbar.push(
            new ToolbarButtonParameters('ADD_NEIGHBOURS', 'code-branch', 'Ajouter des Voisins', this.networkState.getValue().ADD_NEIGHBOURS)
        );
        newToolbar.push(new ToolbarButtonParameters('DELETE_NODES', 'times', 'Supprimer des Nodes'));
        newToolbar.push(
            new ToolbarButtonParameters('CLUSTER_NODES', 'dot-circle', 'Activer le clustering', this.networkState.getValue().CLUSTER_NODES)
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'PHYSICS_ENABLED',
                'magic',
                'Activer le positionnement libre',
                this.networkState.getValue().PHYSICS_ENABLED
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'LAYOUT_FREE',
                'hand-paper',
                'Activer le positionnement libre',
                this.networkState.getValue().LAYOUT_FREE
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters('LAYOUT_MENU', 'arrows-alt', 'Changer la disposition', false, [
                new ToolbarButtonParameters(
                    'LAYOUT_DIR_UD',
                    'arrow-circle-down',
                    'Haut-Bas',
                    this.networkState.getValue().LAYOUT_DIR === 'UD'
                ),
                new ToolbarButtonParameters(
                    'LAYOUT_DIR_DU',
                    'arrow-circle-up',
                    'Bas-Haut',
                    this.networkState.getValue().LAYOUT_DIR === 'DU'
                ),
                new ToolbarButtonParameters(
                    'LAYOUT_DIR_LR',
                    'arrow-circle-right',
                    'Gauche-Droite',
                    this.networkState.getValue().LAYOUT_DIR === 'LR'
                ),
                new ToolbarButtonParameters(
                    'LAYOUT_DIR_RL',
                    'arrow-circle-left',
                    'Droite-Gauche',
                    this.networkState.getValue().LAYOUT_DIR === 'RL'
                )
            ])
        );
        return newToolbar;
    }

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
export const IMAGE_URL_RAW = '../../../content/images/rawdata.svg';
export const IMAGE_URL_SELECTED_RAW = '../../../content/images/rawdata_selected.svg';
export const IMAGE_URL_GEOMARKER = '../../../content/images/geo-marker.svg';
export const IMAGE_URL_SELECTED_GEOMARKER = '../../../content/images/geo-marker-selected.svg';
export const MOCK_GRAPH_DATA = {
    nodes: [
        { id: 1, mongoId: '1a', label: 'Bobby', title: 'Personne', type: 'Biographics' },
        { id: 2, mongoId: '2a', label: 'Explosion', title: 'Evenement', type: 'Event' },
        { id: 3, mongoId: '3a', label: 'Voiture', title: 'Equipement', type: 'Equipment' },
        { id: 4, mongoId: '4a', label: 'Brian', title: 'Personne', type: 'Biographics' },
        { id: 5, mongoId: '5a', label: 'AK-47', title: 'Equipement', type: 'Equipment' },
        { id: 6, mongoId: '6a', label: 'Attentat', title: 'Evenement', type: 'Event' },
        { id: 7, mongoId: '7a', label: 'Raoul', title: 'Personne', type: 'Biographics' },
        { id: 8, mongoId: '8a', label: 'RawData', title: 'Donnee brute', type: 'RawData' },
        { id: 9, mongoId: '9a', label: 'Rencontre', title: 'Evenement', type: 'Event' },
        { id: 10, mongoId: '10a', label: 'RawData', title: 'Donnee brute', type: 'RawData' }
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
