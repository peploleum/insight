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
import WKT from 'ol/format/wkt';

@Injectable({ providedIn: 'root' })
export class MapService {
    public resourceUrl = SERVER_API_URL + 'api/map';

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

    generateChose() {
        return this.http.get(`${SERVER_API_URL}api/generator/bulk`, {
            observe: 'response'
        });
    }

    getGeoJsonFromDto(dto: IMapDataDTO): Feature {
        if (dto.coordinate) {
            const format = new WKT();
            const feature: Feature = new Feature(new Point([dto.coordinate[1], dto.coordinate[0]]));
            format.readFeature(feature.getGeometry(), {
                dataProjection: 'EPSG:4326',
                featureProjection: 'EPSG:3857'
            });
            feature.setId(dto.id);
            feature.set('description', dto.description);
            feature.set('objectType', dto.objectType);
            feature.set('label', dto.label);
            return feature;
        }
        return null;
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
