/**
 * Created by gFolgoas on 18/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { filter, map } from 'rxjs/internal/operators';
import { IMapDataDTO, MapDataDTO, MapSchema } from '../shared/model/map.model';

import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import Geometry from 'ol/geom/geometry';
import proj from 'ol/proj';
import { RawData } from '../shared/model/raw-data.model';
import { FigureStyle, MapLayer, MapState, OlMapProperties, ZoomToFeatureRequest } from '../shared/util/map-utils';
import {
    ENTITY_TYPE_LIST,
    getGenericCoordinatesProperty,
    getGenericNameProperty,
    ToolbarButtonParameters,
    UUID
} from '../shared/util/insight-util';
import { createRequestOption } from '../shared/util/request-util';
import { GenericModel } from '../shared/model/generic.model';
import { QuickViewService } from '../side/quick-view.service';
import { IGraphStructureNodeDTO } from '../shared/model/node.model';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrlMap = SERVER_API_URL + 'api/map';

    featureSource: Subject<Feature[]> = new Subject();
    mapSchema: BehaviorSubject<MapSchema> = new BehaviorSubject(null);

    geoMarkerSource: Subject<Feature[]> = new Subject();
    pinnedGeoMarker: BehaviorSubject<string[]> = new BehaviorSubject([]);

    mapStates: BehaviorSubject<MapState> = new BehaviorSubject(new MapState(true, true, '', false, false, true, ENTITY_TYPE_LIST, 1));
    dessinStates: BehaviorSubject<FigureStyle> = new BehaviorSubject(
        new FigureStyle('Circle', 2, 1, 'rgb(250,5,5)', 'rgba(232,215,43,0.37)')
    );
    mapLayers: BehaviorSubject<MapLayer[]> = new BehaviorSubject(DEFAULT_MAP_LAYERS);
    zoomToLayer: Subject<string> = new Subject();
    zoomToFeature: Subject<ZoomToFeatureRequest> = new Subject();

    mapProperties: OlMapProperties = new OlMapProperties();

    static getMapDataFromGeneric(item: GenericModel): IMapDataDTO {
        let coord: number[] = null;
        const contentField: string[] = ['equipmentDescription', 'eventDescription', 'organisationDescrption', 'rawDataContent'];
        const content: string = contentField.map(key => item[key]).find(r => !!r);
        const coordinate: string = item[getGenericCoordinatesProperty(item)];
        const name: string = item[getGenericNameProperty(item)];

        if (coordinate) {
            const str: string[] = coordinate.split(',');
            coord = str.map(i => parseFloat(i));
        }
        return new MapDataDTO(
            item['id'],
            item['externalId'],
            name || 'defaultName',
            item['entityType'] || 'RawData',
            content || 'defaultContent',
            coord,
            item['geometry'],
            item['properties'] || null
        );
    }

    static getGeoJsonFromDto(dto: IMapDataDTO): Feature {
        if (dto.coordinate || dto.geometry) {
            const featGeom: Geometry =
                this.getFeatureGeometry(dto.geometry) || new Point(proj.fromLonLat([dto.coordinate[1], dto.coordinate[0]]));
            const feature: Feature = new Feature(featGeom);
            feature.setId(dto.id);
            feature.set('description', dto.description);
            feature.set('objectType', dto.objectType);
            feature.set('label', dto.label);
            feature.set('externalId', dto.externalId);
            feature.set('visible', true);
            return feature;
        }
        return null;
    }

    /**
     * geometry => DTO pour le mapping d'un InsightShape
     * */
    static getFeatureGeometry(geometry: any): Geometry {
        if (!geometry) {
            return null;
        }
        let correctCoord;
        let featGeometry: Geometry;
        switch (geometry.type) {
            case 'point':
                correctCoord = proj.fromLonLat([geometry.geometries[0].coordinates[0], geometry.geometries[0].coordinates[1]]);
                featGeometry = new Point(correctCoord);
                break;
            case 'linestring':
                break;
            case 'polygon':
                break;
            case 'geometrycollection':
                break;
        }
        return featGeometry;
    }

    constructor(private http: HttpClient, private _qvs: QuickViewService) {}

    /**
     * Transforme les GenericModel et les envoie dans featureSource
     * */
    getFeaturesFromGeneric(source: GenericModel[], target: 'SEARCH' | 'MAIN'): void {
        if (source && source.length) {
            if (target === 'MAIN') {
                this.sendToMapFeatureSource(source.map(item => MapService.getMapDataFromGeneric(item)));
            } else if (target === 'SEARCH') {
                this.sendSearchToMapFeatureSource(source.map(item => MapService.getMapDataFromGeneric(item)));
            }
        }
    }

    /**
     * Envoie les geoMarkers dans geoMarkerSource
     * */
    getFeaturesFromGeoMarker(source: IMapDataDTO[]): void {
        if (source) {
            this.sendToMapGeoMarkerSource(source);
        }
    }

    sendToMapFeatureSource(source: IMapDataDTO[]): void {
        this.featureSource.next(
            source
                .map(item => MapService.getGeoJsonFromDto(item))
                .filter(dto => dto !== null)
                .map((f: Feature) => {
                    f.set('SEARCH', false);
                    f.set('relationOrder', this.mapSchema.getValue().getEntityLevel(f.getId()));
                    return f;
                })
        );
    }

    sendSearchToMapFeatureSource(source: IMapDataDTO[]): void {
        this.featureSource.next(
            source
                .map(item => MapService.getGeoJsonFromDto(item))
                .filter((feat: Feature) => feat !== null)
                .map((feat: Feature) => {
                    feat.set('SEARCH', true);
                    return feat;
                })
        );
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
            .get<IMapDataDTO[]>(`${this.resourceUrlMap}/_search/georef`, {
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
