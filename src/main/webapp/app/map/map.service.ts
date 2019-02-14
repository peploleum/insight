/**
 * Created by gFolgoas on 18/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { BehaviorSubject, Subject } from 'rxjs';
import { filter, map } from 'rxjs/internal/operators';
import { IMapDataDTO, MapDataDTO } from '../shared/model/map.model';

import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import proj from 'ol/proj';
import { IMAGE_URL_BIO, IMAGE_URL_DEFAULT, IMAGE_URL_EQUIP, IMAGE_URL_RAW } from '../network/network.service';
import { IRawData, RawData } from '../shared/model/raw-data.model';
import { EventThreadResultSet, FigureStyle, MapLayer, MapState } from '../shared/util/map-utils';
import { UUID } from '../shared/util/insight-util';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrl = SERVER_API_URL + 'api/map';

    featureSource: Subject<Feature[]> = new Subject();
    outsideFeatureSelector: Subject<string[]> = new Subject();
    insideFeatureSelector: Subject<string[]> = new Subject();

    actionEmitter: Subject<string> = new Subject();

    rawDataStream: BehaviorSubject<EventThreadResultSet> = new BehaviorSubject(new EventThreadResultSet([], []));

    mapStates: BehaviorSubject<MapState> = new BehaviorSubject(new MapState(true, true, 'all', false));
    dessinStates: BehaviorSubject<FigureStyle> = new BehaviorSubject(
        new FigureStyle('Circle', 2, 1, 'rgb(250,5,5)', 'rgba(232,215,43,0.37)')
    );
    mapLayers: BehaviorSubject<MapLayer[]> = new BehaviorSubject(DEFAULT_MAP_LAYERS);
    zoomToLayer: Subject<string> = new Subject();

    static getImageIconUrl(objectType: string): string {
        switch (objectType) {
            case 'RawData':
                return IMAGE_URL_RAW;
            case 'Equipment':
                return IMAGE_URL_EQUIP;
            case 'Location':
                return IMAGE_URL_RAW;
            case 'Biographics':
                return IMAGE_URL_BIO;
            case 'Organisation':
                return IMAGE_URL_DEFAULT;
            case 'Event':
                return IMAGE_URL_DEFAULT;
            default:
                return null;
        }
    }

    static getMapDataFromRaw(raw: IRawData): IMapDataDTO {
        let coord: number[] = null;
        if (raw.rawDataCoordinates) {
            const str: string[] = raw.rawDataCoordinates.split(',');
            coord = str.map(i => parseFloat(i));
        }
        return new MapDataDTO(raw.id, raw.rawDataName, 'RawData', raw.rawDataContent, coord);
    }

    static getGeoJsonFromDto(dto: IMapDataDTO): Feature {
        if (dto.coordinate) {
            const correctCoord = proj.fromLonLat([dto.coordinate[1], dto.coordinate[0]]);
            const feature: Feature = new Feature(new Point(correctCoord));
            feature.setId(dto.id);
            feature.set('description', dto.description);
            feature.set('objectType', dto.objectType);
            feature.set('label', dto.objectType);
            return feature;
        }
        return null;
    }

    constructor(private http: HttpClient) {}

    getFeaturesFromIds(ids: string[]): void {
        this.http
            .post<IMapDataDTO[]>(`${this.resourceUrl}`, ids, {
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<IMapDataDTO[]>) => res.ok),
                map(res => {
                    return <IMapDataDTO[]>res.body;
                })
            )
            .subscribe((data: IMapDataDTO[]) => {
                this.sendToMap(data);
            });
    }

    /**
     * Transforme les RawData et les envoie dans featureSource
     * */
    getFeaturesFromRawData(source: IRawData[]): void {
        if (source && source.length) {
            this.sendToMap(source.map(item => MapService.getMapDataFromRaw(item)));
        }
    }

    sendToMap(source: IMapDataDTO[]): void {
        this.featureSource.next(source.map(item => MapService.getGeoJsonFromDto(item)).filter(dto => dto !== null));
    }

    getIconImage(uri: string, id: string): string {
        const svg =
            '<svg width="120" height="120" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
            '<defs>' +
            `<clipPath id="${id}">` +
            '<circle cx="60" cy="60" r="60" fill="red" />' +
            '</clipPath>' +
            '</defs>' +
            `<image width="100%" height="100%" xlink:href="${uri}" clip-path="url(%23${id})" />` +
            '</svg>';
        return svg;
    }

    /** Temporaire: Remplace swagger */
    generateRawData() {
        return this.http.get(`${SERVER_API_URL}api/generator/bulk`, {
            observe: 'response'
        });
    }
}

export const DEFAULT_MAP_LAYERS: MapLayer[] = [
    {
        layerId: UUID(),
        layerName: 'DrawingLayer1',
        layerType: 'DESSIN',
        visible: true,
        layerStatus: 'NEW',
        layerZIndex: 1,
        selected: true
    },
    {
        layerId: UUID(),
        layerName: 'OSM',
        layerType: 'WMS',
        layerStatus: 'NEW',
        layerZIndex: 0,
        visible: true
    },
    {
        layerId: UUID(),
        layerName: 'BingMaps',
        layerType: 'WMS',
        layerStatus: 'NEW',
        layerZIndex: 0,
        visible: false
    }
];

export const GEO_JSON_OBJECT = {
    type: 'FeatureCollection',
    crs: {
        type: 'name',
        properties: {
            name: 'EPSG:3857'
        }
    },
    features: [
        {
            type: 'Feature',
            geometry: {
                type: 'Point',
                coordinates: [0, 0]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'LineString',
                coordinates: [[4e6, -2e6], [8e6, 2e6]]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'LineString',
                coordinates: [[4e6, 2e6], [8e6, -2e6]]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'Polygon',
                coordinates: [[[-5e6, -1e6], [-4e6, 1e6], [-3e6, -1e6]]]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'MultiLineString',
                coordinates: [
                    [[-1e6, -7.5e5], [-1e6, 7.5e5]],
                    [[1e6, -7.5e5], [1e6, 7.5e5]],
                    [[-7.5e5, -1e6], [7.5e5, -1e6]],
                    [[-7.5e5, 1e6], [7.5e5, 1e6]]
                ]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'MultiPolygon',
                coordinates: [
                    [[[-5e6, 6e6], [-5e6, 8e6], [-3e6, 8e6], [-3e6, 6e6]]],
                    [[[-2e6, 6e6], [-2e6, 8e6], [0, 8e6], [0, 6e6]]],
                    [[[1e6, 6e6], [1e6, 8e6], [3e6, 8e6], [3e6, 6e6]]]
                ]
            }
        },
        {
            type: 'Feature',
            geometry: {
                type: 'GeometryCollection',
                geometries: [
                    {
                        type: 'LineString',
                        coordinates: [[-5e6, -5e6], [0, -5e6]]
                    },
                    {
                        type: 'Point',
                        coordinates: [4e6, -5e6]
                    },
                    {
                        type: 'Polygon',
                        coordinates: [[[1e6, -6e6], [2e6, -4e6], [3e6, -6e6]]]
                    }
                ]
            }
        }
    ]
};
