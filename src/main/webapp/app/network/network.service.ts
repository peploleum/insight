/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Observable, of } from 'rxjs';
import { IdType } from 'vis';
import { map } from 'rxjs/internal/operators';
import { EdgeDTO, GraphDataCollection, GraphyNodeDTO, IGraphyNodeDTO, NodeDTO } from 'app/shared/model/node.model';
import { UUID } from '../shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class NetworkService {
    private resourceUrl;

    public static getNodeDto(label: string, objectType?: string, id?: IdType, title?: string, imageUrl?: string): NodeDTO {
        const image: string = imageUrl ? imageUrl : NetworkService.getNodeImageUrl(objectType);
        const color = {
            border: NetworkService.getNodeColor(objectType)
        };
        const font = {
            color: 'white'
        };
        return new NodeDTO(label, id, title, image, color, 3, font);
    }

    public static getEdgeCollection(idOrigin: IdType, nodes: NodeDTO[]): EdgeDTO[] {
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

    constructor(private http: HttpClient) {
        if (DEBUG_INFO_ENABLED) {
            this.resourceUrl = 'http://' + window.location.hostname + ':8090/';
        } else {
            this.resourceUrl = 'http://10.65.34.149:30200/';
        }
    }

    getMockGraphData(): Observable<GraphDataCollection> {
        return of(MOCK_GRAPH_DATA).pipe(
            map(data => {
                const dataCollection = new GraphDataCollection([], []);
                dataCollection.nodes = data['nodes'].map(item =>
                    NetworkService.getNodeDto(item['label'], item['type'], <IdType>item['id'], item['title'])
                );
                dataCollection.edges = data['edges'].map(item => NetworkService.getEdgeDto(<IdType>item['from'], <IdType>item['to']));
                return dataCollection;
            })
        );
    }

    getGraphData(idOrigin: IdType): Observable<GraphDataCollection> {
        const headers = new HttpHeaders();
        const url = DEBUG_INFO_ENABLED ? 'api/traversal/mock/' : 'api/traversal/';
        return this.http.get(`${this.resourceUrl}` + url + `${idOrigin}`, { headers, observe: 'response' }).pipe(
            map((res: HttpResponse<IGraphyNodeDTO[]>) => {
                const body: IGraphyNodeDTO[] = res.body;
                const data = new GraphDataCollection([], []);
                data.nodes = body.map((item: IGraphyNodeDTO) => NetworkService.getNodeDto(item.label, item.type, item.id));
                data.edges = NetworkService.getEdgeCollection(idOrigin, data.nodes);
                return data;
            })
        );
    }

    getNodeProperties(idOrigin: IdType): Observable<HttpResponse<IGraphyNodeDTO>> {
        const headers = new HttpHeaders();
        if (DEBUG_INFO_ENABLED) {
            /** Tant que l'endpoint de graphy n'est pas dispo */
            const fakeResponse: HttpResponse<IGraphyNodeDTO> = new HttpResponse({
                body: new GraphyNodeDTO(UUID(), 'Personne Origine', 'Biographics'),
                headers: new HttpHeaders(),
                status: 200
            });
            return of(fakeResponse);
        }
        const url = DEBUG_INFO_ENABLED ? 'api/traversal/mock/properties/' : 'api/traversal/properties/';
        return this.http.get<IGraphyNodeDTO>(`${this.resourceUrl}` + url + `${idOrigin}`, {
            headers,
            observe: 'response'
        });
    }
}

export const IMAGE_URL_BIO = 'https://img.icons8.com/color/1600/person-male.png';
export const IMAGE_URL_EVENT =
    'https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Explosion-155624_icon.svg/2000px-Explosion-155624_icon.svg.png';
export const IMAGE_URL_EQUIP = 'http://cdn.onlinewebfonts.com/svg/img_236380.png';
export const IMAGE_URL_RAW = 'https://cdn3.iconfinder.com/data/icons/analytics-4/500/Analytic-49-512.png';
export const IMAGE_URL_DEFAULT =
    'https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/Information_icon_with_gradient_background.svg/1024px-Information_icon_with_gradient_background.svg.png';
export const MOCK_GRAPH_DATA = {
    nodes: [
        { id: 1, label: 'Bobby', title: 'Personne', type: 'Biographics' },
        { id: 2, label: 'Explosion', title: 'Evenement', type: 'Event' },
        { id: 3, label: 'Voiture', title: 'Equipement', type: 'Equipment' },
        { id: 4, label: 'Brian', title: 'Personne', type: 'Biographics' },
        { id: 5, label: 'AK-47', title: 'Equipement', type: 'Equipment' },
        { id: 6, label: 'Attentat', title: 'Evenement', type: 'Event' },
        { id: 7, label: 'Raoul', title: 'Personne', type: 'Biographics' },
        { id: 8, label: 'RawData', title: 'Donnee brute', type: 'RawData' },
        { id: 9, label: 'Rencontre', title: 'Evenement', type: 'Event' },
        { id: 10, label: 'RawData', title: 'Donnee brute', type: 'RawData' }
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
