/**
 * Created by gFolgoas on 18/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { filter, map } from 'rxjs/internal/operators';
import { IMapDataDTO, MapDataDTO } from '../shared/model/map.model';

import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import proj from 'ol/proj';
import { IRawData, RawData } from '../shared/model/raw-data.model';
import { FigureStyle, MapLayer, MapState, ZoomToFeatureRequest } from '../shared/util/map-utils';
import { ToolbarButtonParameters, UUID } from '../shared/util/insight-util';
import { createRequestOption } from '../shared/util/request-util';
import { CONTENT_FIELD, COORDINATE_FIELD, GenericModel, NAME_FIELD } from '../shared/model/generic.model';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrl = SERVER_API_URL + 'api/map';

    featureSource: Subject<Feature[]> = new Subject();

    geoMarkerSource: Subject<Feature[]> = new Subject();
    pinnedGeoMarker: BehaviorSubject<string[]> = new BehaviorSubject([]);

    mapStates: BehaviorSubject<MapState> = new BehaviorSubject(new MapState(true, true, '', false, false));
    dessinStates: BehaviorSubject<FigureStyle> = new BehaviorSubject(
        new FigureStyle('Circle', 2, 1, 'rgb(250,5,5)', 'rgba(232,215,43,0.37)')
    );
    mapLayers: BehaviorSubject<MapLayer[]> = new BehaviorSubject(DEFAULT_MAP_LAYERS);
    zoomToLayer: Subject<string> = new Subject();
    zoomToFeature: Subject<ZoomToFeatureRequest> = new Subject();

    static getMapDataFromRaw(raw: IRawData): IMapDataDTO {
        let coord: number[] = null;
        if (raw.rawDataCoordinates) {
            const str: string[] = raw.rawDataCoordinates.split(',');
            coord = str.map(i => parseFloat(i));
        }
        return new MapDataDTO(raw.id, raw.rawDataName, 'RawData', raw.rawDataContent, coord);
    }

    static getMapDataFromGeneric(item: GenericModel): IMapDataDTO {
        let coord: number[] = null;
        const nonNullKeys: string[] = Object.keys(item).filter(key => item[key] !== null && typeof item[key] !== 'undefined');
        const name: string[] = nonNullKeys.filter(key => NAME_FIELD.indexOf(key) !== -1).map(key => item[key]);
        const coordinate: string[] = nonNullKeys.filter(key => COORDINATE_FIELD.indexOf(key) !== -1).map(key => item[key]);
        const content: string[] = nonNullKeys.filter(key => CONTENT_FIELD.indexOf(key) !== -1).map(key => item[key]);
        if (coordinate[0]) {
            const str: string[] = coordinate[0].split(',');
            coord = str.map(i => parseFloat(i));
        }
        return new MapDataDTO(item['id'], name[0] || 'defaultName', 'RawData', content[0] || 'defaultContent', coord);
    }

    static getGeoJsonFromDto(dto: IMapDataDTO): Feature {
        if (dto.coordinate) {
            const correctCoord = proj.fromLonLat([dto.coordinate[1], dto.coordinate[0]]);
            const feature: Feature = new Feature(new Point(correctCoord));
            feature.setId(dto.id);
            feature.set('description', dto.description);
            feature.set('objectType', dto.objectType);
            feature.set('label', dto.label);
            return feature;
        }
        return null;
    }

    constructor(private http: HttpClient) {}

    /**
     * Transforme les GenericModel et les envoie dans featureSource
     * */
    getFeaturesFromGeneric(source: GenericModel[]): void {
        if (source && source.length) {
            this.sendToMapFeatureSource(source.map(item => MapService.getMapDataFromGeneric(item)));
        }
    }

    /**
     * Envoie les geoMarker dans geoMarkerSource
     * */
    getFeaturesFromGeoMarker(source: IMapDataDTO[]): void {
        if (source) {
            this.sendToMapGeoMarkerSource(source);
        }
    }

    sendToMapFeatureSource(source: IMapDataDTO[]): void {
        this.featureSource.next(source.map(item => MapService.getGeoJsonFromDto(item)).filter(dto => dto !== null));
    }

    sendToMapGeoMarkerSource(source: IMapDataDTO[]): void {
        source.forEach(item => {
            if (!item.properties) {
                item.properties = {};
            }
            item.properties['ADDED_TO_MAP'] = true;
        });
        this.geoMarkerSource.next(source.map(item => MapService.getGeoJsonFromDto(item)).filter(dto => dto !== null));
    }

    getGeoMarker(search: string): Observable<IMapDataDTO[]> {
        const req = { query: search };
        const options = createRequestOption(req);
        return this.http
            .get<IMapDataDTO[]>(`${this.resourceUrl}/_search/georef`, {
                params: options,
                observe: 'response'
            })
            .pipe(
                filter((res: HttpResponse<any[]>) => res.ok),
                map((res: HttpResponse<any[]>) => res.body)
            );
    }

    getUpdatedEventThreadToolbar(): ToolbarButtonParameters[] {
        const newToolbar = [];
        newToolbar.push(
            new ToolbarButtonParameters(
                'F_ALL_DATA',
                'user-circle',
                'Voir toutes les données localisées',
                this.mapStates.getValue().FILTER_TYPE === 'all'
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'F_LOCATIONS_ONLY',
                'bullseye',
                'Voir les localités uniquement',
                this.mapStates.getValue().FILTER_TYPE === 'locations'
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'F_IMAGES_ONLY',
                'image',
                'Voir les données avec images uniquement',
                this.mapStates.getValue().FILTER_TYPE === 'images'
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'F_NO_FILTER',
                'ban',
                'Voir toutes les données (Aucun filtre)',
                this.mapStates.getValue().FILTER_TYPE === ''
            )
        );
        newToolbar.push(
            new ToolbarButtonParameters(
                'AUTO_REFRESH',
                'sync-alt',
                'Activer le rafraîchissement automatique',
                this.mapStates.getValue().AUTO_REFRESH
            )
        );
        return newToolbar;
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
