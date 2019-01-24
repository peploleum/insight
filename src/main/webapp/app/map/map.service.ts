/**
 * Created by gFolgoas on 18/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/internal/operators';
import { IMapDataDTO } from '../shared/model/map.model';

import Feature from 'ol/feature';
import Point from 'ol/geom/point';
import proj from 'ol/proj';
import { IMAGE_URL_BIO, IMAGE_URL_DEFAULT, IMAGE_URL_EQUIP, IMAGE_URL_RAW } from '../network/network.service';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrl = SERVER_API_URL + 'api/map';

    static getImageIconUrl(objectType: string): string {
        switch (objectType) {
            case 'RawData':
                return IMAGE_URL_RAW;
            case 'Equipement':
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
                return IMAGE_URL_DEFAULT;
        }
    }

    constructor(private http: HttpClient) {}

    getFeaturesForIds(ids: string[]): Observable<Feature[]> {
        return this.getData(ids).pipe(
            filter((res: HttpResponse<IMapDataDTO[]>) => res.ok),
            map(res => {
                const data: IMapDataDTO[] = <IMapDataDTO[]>res.body;
                return data.map(dto => this.getGeoJsonFromDto(dto)).filter(dto => dto !== null);
            })
        );
    }

    getData(ids: string[]): Observable<HttpResponse<IMapDataDTO[]>> {
        if (DEBUG_INFO_ENABLED) {
            return this.getAllData();
        }
        return this.http.post<IMapDataDTO[]>(`${this.resourceUrl}`, ids, {
            observe: 'response'
        });
    }

    getAllData(): Observable<HttpResponse<IMapDataDTO[]>> {
        return this.http.get<IMapDataDTO[]>(`${this.resourceUrl}` + `/get-all-data`, {
            observe: 'response'
        });
    }

    sendToMap(source: Observable<IMapDataDTO[]>): Observable<Feature[]> {
        return source.pipe(
            map((data: IMapDataDTO[]) => {
                return data.map(dto => this.getGeoJsonFromDto(dto)).filter(dto => dto !== null);
            })
        );
    }

    rawDataToMapDataDTO() {}

    /** Temporaire: Remplace swagger */
    generateRawData() {
        return this.http.get(`${SERVER_API_URL}api/generator/bulk`, {
            observe: 'response'
        });
    }

    getGeoJsonFromDto(dto: IMapDataDTO): Feature {
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
}

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
