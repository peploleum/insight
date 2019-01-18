/**
 * Created by gFolgoas on 18/01/2019.
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { DEBUG_INFO_ENABLED, SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { IMapDataDTO } from '../shared/model/map.model';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { GeoJSON } from 'ol/format';
import { filter, map } from 'rxjs/internal/operators';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrl = SERVER_API_URL + 'api/map';

    constructor(private http: HttpClient) {}

    getFeaturesForIds(ids: string[]): Observable<Feature[]> {
        return this.getData(ids).pipe(
            filter((res: HttpResponse<IMapDataDTO[]>) => res.ok),
            map(res => {
                const data: IMapDataDTO[] = <IMapDataDTO[]>res.body;
                return data.map(dto => this.getGeoJsonFromDto(dto));
            })
        );
    }

    getRelationIds(idOrigin: string): Observable<HttpResponse<string[]>> {
        return this.http.get<string[]>(`${this.resourceUrl}` + `${idOrigin}`, { observe: 'response' });
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

    getGeoJsonFromDto(dto: IMapDataDTO): any {
        const feature = {
            geometry: {
                type: 'Point',
                coordinates: dto.coordinate
            },
            name: dto.label,
            properties: {
                id: dto.id,
                description: dto.description,
                objectType: dto.objectType
            }
        };
        // feature.set('id', dto.id);
        // feature.set('description', dto.description);
        // feature.set('objectType', dto.objectType);
        return feature;
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
