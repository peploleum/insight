/**
 * Created by gFolgoas on 14/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import { Observable, of } from 'rxjs';
import { Edge, IdType, Node } from 'vis';
import { map } from 'rxjs/internal/operators';

@Injectable({ providedIn: 'root' })
export class NetworkService {
    private resourceUrl;

    static getNodeDto(label: string, objectType?: string, id?: IdType, title?: string, imageUrl?: string): Node {
        return {
            id: id,
            label: label,
            title: title,
            image: imageUrl ? imageUrl : NetworkService.getNodeImageUrl(objectType),
            color: {
                border: NetworkService.getNodeColor(objectType)
            },
            borderWidth: 3,
            font: {
                color: 'white'
            }
        };
    }

    static getEdgeCollection(idOrigin: IdType, nodes: Node[]): Edge[] {
        return nodes.map(node => NetworkService.getEdgeDto(idOrigin, node.id));
    }

    static getEdgeDto(from: IdType, to: IdType): Edge {
        return {
            from: from,
            to: to
        };
    }

    static getNodeColor(objectType: string): string {
        let color: string;
        switch (objectType) {
            case 'PERSONNE':
                color = 'red';
                break;
            case 'EVENT':
                color = 'blue';
                break;
            default:
                color = 'pink';
                break;
        }
        return color;
    }

    static getNodeImageUrl(objectType: string): string {
        switch (objectType) {
            case 'PERSONNE':
                return IMAGE_URL_PERSONNE;
                break;
            case 'EVENT':
                return IMAGE_URL_EVENT;
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

    getMockGraphData(): Observable<any> {
        return of(MOCK_GRAPH_DATA).pipe(
            map(data => {
                data['nodes'] = data['nodes'].map(item =>
                    NetworkService.getNodeDto(item.label, item.type, <IdType>item.id, item.title, null)
                );
                data['edges'] = data['edges'].map(item => NetworkService.getEdgeDto(<IdType>item.from, <IdType>item.to));
                return data;
            })
        );
    }

    getGraphData(idOrigin: IdType): Observable<any> {
        const headers = new HttpHeaders();
        return this.http.get(`${this.resourceUrl}api/traversal/mock/${idOrigin}`, { headers, observe: 'response' }).pipe(
            map(res => {
                const data = {};
                data['nodes'] = res.body.map(item => NetworkService.getNodeDto(item.label, item.type, <IdType>item.id));
                data['edges'] = NetworkService.getEdgeCollection(idOrigin, data['nodes']);
                return data;
            })
        );
    }
}
export const IMAGE_URL_PERSONNE = 'https://parismatch.be/app/uploads/2018/04/Macaca_nigra_self-portrait_large-e1524567086123-1100x715.jpg';
export const IMAGE_URL_EVENT = 'https://fridg-front.s3.amazonaws.com/media/products/banane_DAC0XAQ.jpg';
export const IMAGE_URL_DEFAULT = 'http://www.stickersnews.fr/7212-10878-thickbox/sticker-enfant-arbre-animaux-de-la-jungle.jpg';
export const MOCK_GRAPH_DATA = {
    nodes: [
        { id: 1, label: 'node 1', title: 'bidou 1', type: 'PERSONNE' },
        { id: 2, label: 'node 2', title: 'bidou 2', type: 'PERSONNE' },
        { id: 3, label: 'node 3', title: 'bidou 3', type: 'EVENT' },
        { id: 4, label: 'node 4', title: 'bidou 4', type: 'PERSONNE' },
        { id: 5, label: 'node 5', title: 'bidou 5', type: 'EVENT' },
        { id: 6, label: 'node 6', title: 'bidou 6', type: 'EVENT' },
        { id: 7, label: 'node 7', title: 'bidou 7', type: 'PERSONNE' },
        { id: 8, label: 'node 8', title: 'bidou 8', type: 'EVENT' },
        { id: 9, label: 'node 9', title: 'bidou 9', type: 'LOCATION' },
        { id: 10, label: 'node 10', title: 'bidou 10', type: 'LOCATION' }
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
