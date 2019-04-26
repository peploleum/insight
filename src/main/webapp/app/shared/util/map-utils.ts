/**
 * Created by gFolgoas on 22/01/2019.
 */
import { IRawData } from '../model/raw-data.model';
import Style from 'ol/style/style';
import Feature from 'ol/feature';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Icon from 'ol/style/icon';
import Extent from 'ol/extent';
import GeometryCollection from 'ol/geom/geometrycollection';
import LineString from 'ol/geom/linestring';
import Point from 'ol/geom/point';
import {
    IMAGE_URL_BIO,
    IMAGE_URL_DEFAULT,
    IMAGE_URL_EQUIP,
    IMAGE_URL_GEOMARKER,
    IMAGE_URL_RAW,
    IMAGE_URL_SELECTED_GEOMARKER,
    IMAGE_URL_SELECTED_RAW
} from '../../network/network.service';
import { GenericModel } from '../model/generic.model';
import { Moment } from 'moment';

export class MapState {
    DISPLAY_LABEL: boolean;
    DISPLAY_CONTENT_ON_HOVER: boolean;
    FILTER_TYPE: string;
    DESSIN_ENABLED: boolean;
    AUTO_REFRESH: boolean;

    constructor(
        DISPLAY_LABEL: boolean,
        DISPLAY_CONTENT_ON_HOVER: boolean,
        FILTER_TYPE: string,
        DESSIN_ENABLED: boolean,
        AUTO_REFRESH: boolean
    ) {
        this.DISPLAY_LABEL = DISPLAY_LABEL;
        this.DISPLAY_CONTENT_ON_HOVER = DISPLAY_CONTENT_ON_HOVER;
        this.FILTER_TYPE = FILTER_TYPE;
        this.DESSIN_ENABLED = DESSIN_ENABLED;
        this.AUTO_REFRESH = AUTO_REFRESH;
    }
}

export class EventThreadResultSet {
    data: IRawData[];
    dataIds: string[];

    constructor(data: IRawData[], dataIds: string[]) {
        this.data = data;
        this.dataIds = dataIds;
    }

    clearAll() {
        this.data = [];
        this.dataIds = [];
    }
}

export type MapLayerType = 'DESSIN' | 'KML' | 'WMS';
export type MapLayerStatus = 'NEW' | 'UPDATE' | 'DELETE';

export class MapLayer {
    layerId: string;
    layerName: string;
    layerType: MapLayerType;
    visible: boolean;
    layerStatus: MapLayerStatus;
    layerZIndex: number;
    selected?: boolean;
    properties?: {};

    constructor(
        layerId: string,
        layerName: string,
        layerType: MapLayerType,
        visible: boolean,
        layerZIndex: number,
        selected?: boolean,
        properties?: {}
    ) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.layerType = layerType;
        this.visible = visible;
        this.layerStatus = 'NEW';
        this.layerZIndex = layerZIndex;
        this.selected = selected;
        this.properties = properties ? properties : {};
    }
}

export class FigureStyle {
    form: any;
    size: number;
    type: number;
    strokeColor: string;
    fillColor?: string;

    constructor(form: any, size: number, type: number, strokeColor: string, fillColor?: string) {
        this.form = form;
        this.size = size;
        this.type = type;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }
}

export const getlayerIcon = (layerType: string): string => {
    switch (layerType) {
        case 'DESSIN':
            return 'pencil-alt';
        case 'KML':
            return 'map-marked-alt';
        case 'WMS':
            return 'globe';
    }
};

export class ZoomToFeatureRequest {
    targetLayer: string;
    featureId: string;

    constructor(targetLayer: string, featureId: string) {
        this.targetLayer = targetLayer;
        this.featureId = featureId;
    }
}

export class MapOverlayGenericMapper {
    entityType?: string;
    title?: string;
    subTitle?: string;
    image?: any;
    content?: string;
    date?: Moment;

    static fromGeneric(entity: GenericModel): MapOverlayGenericMapper {
        const mapper: MapOverlayGenericMapper = new MapOverlayGenericMapper();
        mapper.entityType = entity['entityType'];
        mapper.title =
            entity['biographicsName'] ||
            entity['equipmentName'] ||
            entity['eventName'] ||
            entity['locationName'] ||
            entity['organisationName'] ||
            entity['rawDataName'] ||
            'noTitle';
        mapper.subTitle = entity['biographicsFirstname'] || entity['rawDataSubType'] || 'noSubTitle';
        mapper.image =
            entity['biographicsImage'] ||
            entity['equipmentImage'] ||
            entity['eventImage'] ||
            entity['locationImage'] ||
            entity['organisationImage'] ||
            entity['rawDataData'];
        mapper.content =
            entity['biographicsName'] ||
            entity['equipmentDescription'] ||
            entity['eventDescription'] ||
            entity['locationName'] ||
            entity['organisationDescrption'] ||
            entity['rawDataContent'] ||
            'noContent';
        mapper.date = entity['rawDataCreationDate'];
        return mapper;
    }

    constructor(entityType?: string, title?: string, subTitle?: string, image?: any, content?: string, date?: Moment) {
        this.entityType = entityType;
        this.title = title;
        this.subTitle = subTitle;
        this.image = image;
        this.content = content;
        this.date = date;
    }
}

/**
 * Fonction de style principale, maxFeatureCount est utile au calcul de la color des clusters,
 * Si gestion de la selection hors d'une selectInteraction => utiliser la property selected sur les features
 * */
export const insStyleFunction = (feature: Feature, resolution: number, maxFeatureCount: number): Style[] => {
    let style: Style[] = [];
    const features: Feature[] = feature.get('features');
    const isCluster: boolean = features.length > 1;
    const selected: boolean = isCluster ? !!features.find(f => f.get('selected')) : features[0].get('selected');
    if (isCluster) {
        style = selected ? insSelectedClusterStyleFunction(feature, maxFeatureCount) : insClusterStyleFunction(feature, maxFeatureCount);
    } else {
        const originalFeature = features[0];
        style.push(
            selected
                ? insSelectedBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature)
                : insBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature)
        );
    }
    return style;
};

/**
 * Fonction de style de sélection, maxFeatureCount est utile au calcul de la color des clusters
 * */
export const insSelectedStyleFunction = (feature: Feature, resolution: number, maxFeatureCount: number): Style[] => {
    let style: Style[] = [];
    const isCluster: boolean = feature.get('features').length > 1;
    if (isCluster) {
        style = insSelectedClusterStyleFunction(feature, maxFeatureCount);
    } else {
        const originalFeature = feature.get('features')[0];
        style.push(insSelectedBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature));
    }
    return style;
};

/**
 * Fonction de style on hover, maxFeatureCount est utile au calcul de la color des clusters
 * */
export const insHoveredStyleFunction = (feature: Feature, resolution: number, maxFeatureCount: number): Style[] => {
    let style: Style[] = [];
    const isCluster: boolean = feature.get('features').length > 1;
    if (isCluster) {
        style = expandClusterStyleFunction(feature, resolution);
    } else {
        const originalFeature = feature.get('features')[0];
        style.push(insSelectedBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature));
    }
    return style;
};

/**
 * Applique le text de la feature: label (uniquement si non cluster) à ses fonctions de style
 * */
export const insSetTextStyleFunction = (feature: Feature, styles: Style[]): Style[] => {
    if (feature.get('features').length === 1 && feature.get('features')[0].get('label')) {
        styles.forEach(s => {
            if (s.getText() !== null && typeof s.getText() !== 'undefined') {
                s.getText().setText(feature.get('features')[0].get('label'));
            }
        });
    }
    return styles;
};

/**
 * Fonction de style des clusters
 * */
export const insClusterStyleFunction = (feature: Feature, maxFeatureCount: number): Style[] => {
    const clusterSize = feature.get('features').length;
    const style = new Style({
        image: new Circle({
            radius: feature.get('radius'),
            fill: new Fill({
                color: [255, 153, 0, Math.min(0.8, 0.4 + clusterSize / maxFeatureCount)]
            }),
            stroke: new Stroke({
                color: [255, 153, 0, 1],
                width: 3
            })
        }),
        text: new Text({
            text: clusterSize.toString(),
            fill: new Fill({
                color: '#fff'
            }),
            stroke: new Stroke({
                color: 'rgba(0, 0, 0, 0.6)',
                width: 3
            })
        })
    });
    return [style];
};

/**
 * Fonction de style des clusters selected
 * */
export const insSelectedClusterStyleFunction = (feature: Feature, maxFeatureCount: number): Style[] => {
    const clusterSize = feature.get('features').length;
    const style = new Style({
        image: new Circle({
            radius: feature.get('radius'),
            fill: new Fill({
                color: [128, 255, 128, Math.min(0.8, 0.4 + clusterSize / maxFeatureCount)]
            }),
            stroke: new Stroke({
                color: [128, 255, 128, 1],
                width: 3
            })
        }),
        text: new Text({
            text: clusterSize.toString(),
            fill: new Fill({
                color: '#fff'
            }),
            stroke: new Stroke({
                color: 'rgba(0, 0, 0, 0.6)',
                width: 3
            })
        })
    });
    return [style];
};

/**
 * Set le radius des features en entrée et renvoie la valeur maximale des clusters
 * */
export const setClusterRadiusFromExtent = (features: Feature[], resolution: number): number => {
    let maxFeatureCount = 0;
    for (let i = features.length - 1; i >= 0; --i) {
        const feature = features[i];
        let radius;
        const originalFeatures: Feature[] = feature.get('features');
        const extent = Extent.createEmpty();

        // Void évalue une expression et renvoie undefined
        let j = void 0;
        let jj = void 0;
        for (j = 0, jj = originalFeatures.length; j < jj; ++j) {
            Extent.extend(extent, originalFeatures[j].getGeometry().getExtent());
        }
        maxFeatureCount = Math.max(maxFeatureCount, jj);
        radius = (0.65 * (Extent.getWidth(extent) + Extent.getHeight(extent))) / resolution;
        feature.set('radius', radius);
    }
    return maxFeatureCount;
};

/**
 * Set le radius des features en entrée et renvoie la valeur maximale des clusters
 * */
export const setClusterRadius = (features: Feature[], resolution: number): number => {
    let maxFeatureCount = 0;
    for (let i = features.length - 1; i >= 0; --i) {
        const feature = features[i];
        const originalFeatures: Feature[] = feature.get('features');
        maxFeatureCount = Math.max(maxFeatureCount, originalFeatures.length);
        const radius = Math.min(Math.log10(originalFeatures.length) * 10, 60);
        feature.set('radius', radius);
    }
    return maxFeatureCount;
};

/**
 * Fonction de style de base selon la geometry de la feature
 * */
export const insBaseStyleFunction = (geometryType: string, feature?: Feature): Style => {
    switch (geometryType) {
        case 'Point':
            const objectType = feature ? feature.get('objectType') : '';
            const src: string = getMapImageIconUrl(objectType);
            return new Style({
                image:
                    src === null
                        ? new Circle({
                              radius: 14,
                              fill: new Fill({
                                  color: 'rgba(230, 240, 255, 1)'
                              }),
                              stroke: new Stroke({ color: '#ffc600', width: 3 })
                          })
                        : new Icon({
                              anchor: [0.5, 0.5],
                              scale: 0.05,
                              src: `${src}`
                          }),
                text: new Text({
                    font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
                    placement: 'point',
                    textBaseline: 'top',
                    offsetY: 10,
                    fill: new Fill({
                        color: 'black'
                    })
                })
            });
            break;
        case 'LineString':
            return new Style({
                stroke: new Stroke({
                    color: [255, 153, 0, 0.8],
                    width: 1
                })
            });
            break;
        case 'MultiLineString':
            return new Style({
                stroke: new Stroke({
                    color: [255, 153, 0, 0.8],
                    width: 1
                })
            });
            break;
        case 'MultiPoint':
            return new Style({
                image: new Circle({
                    radius: 14,
                    fill: new Fill({
                        color: 'rgba(230, 240, 255, 1)'
                    }),
                    stroke: new Stroke({ color: '#ffc600', width: 3 })
                })
            });
            break;
        case 'MultiPolygon':
            return new Style({
                stroke: new Stroke({
                    color: 'yellow',
                    width: 1
                }),
                fill: new Fill({
                    color: 'rgba(255, 255, 0, 0.1)'
                })
            });
            break;
        case 'Polygon':
            return new Style({
                stroke: new Stroke({
                    color: 'blue',
                    lineDash: [4],
                    width: 3
                }),
                fill: new Fill({
                    color: 'rgba(0, 0, 255, 0.1)'
                })
            });
            break;
        case 'GeometryCollection':
            return new Style({
                stroke: new Stroke({
                    color: 'magenta',
                    width: 2
                }),
                fill: new Fill({
                    color: 'magenta'
                }),
                image: new Circle({
                    radius: 10,
                    fill: null,
                    stroke: new Stroke({
                        color: 'magenta'
                    })
                })
            });
            break;
        default:
            return new Style({
                stroke: new Stroke({
                    color: 'red',
                    width: 2
                }),
                fill: new Fill({
                    color: 'rgba(255,0,0,0.2)'
                })
            });
            break;
    }
};

export const insSelectedBaseStyleFunction = (geometryType: string, feature?: Feature): Style => {
    switch (geometryType) {
        case 'Point':
            const objectType = feature ? feature.get('objectType') : '';
            const src: string = getSelectedImageIconUrl(objectType);
            return new Style({
                image:
                    src === null
                        ? new Circle({
                              radius: 14,
                              fill: new Fill({
                                  color: 'rgba(230, 240, 255, 1)'
                              }),
                              stroke: new Stroke({ color: '#cb412a', width: 4 })
                          })
                        : new Icon({
                              anchor: [0.5, 0.5],
                              scale: 0.05,
                              src: `${src}`
                          }),
                text: new Text({
                    font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
                    placement: 'point',
                    textBaseline: 'top',
                    offsetY: 10,
                    fill: new Fill({
                        color: 'black'
                    })
                })
            });
            break;
        case 'LineString':
            return new Style({
                stroke: new Stroke({
                    color: 'green',
                    width: 1
                })
            });
            break;
        case 'MultiLineString':
            return new Style({
                stroke: new Stroke({
                    color: 'green',
                    width: 1
                })
            });
            break;
        case 'MultiPoint':
            return new Style({
                image: new Circle({
                    radius: 14,
                    fill: new Fill({
                        color: 'rgba(230, 240, 255, 1)'
                    }),
                    stroke: new Stroke({ color: '#ffc600', width: 3 })
                })
            });
            break;
        case 'MultiPolygon':
            return new Style({
                stroke: new Stroke({
                    color: 'yellow',
                    width: 1
                }),
                fill: new Fill({
                    color: 'rgba(255, 255, 0, 0.1)'
                })
            });
            break;
        case 'Polygon':
            return new Style({
                stroke: new Stroke({
                    color: 'blue',
                    lineDash: [4],
                    width: 3
                }),
                fill: new Fill({
                    color: 'rgba(0, 0, 255, 0.1)'
                })
            });
            break;
        case 'GeometryCollection':
            return new Style({
                stroke: new Stroke({
                    color: 'magenta',
                    width: 2
                }),
                fill: new Fill({
                    color: 'magenta'
                }),
                image: new Circle({
                    radius: 10,
                    fill: null,
                    stroke: new Stroke({
                        color: 'magenta'
                    })
                })
            });
            break;
        default:
            return new Style({
                stroke: new Stroke({
                    color: 'red',
                    width: 2
                }),
                fill: new Fill({
                    color: 'rgba(255,0,0,0.2)'
                })
            });
            break;
    }
};

/**
 * Fonction de style des clusters
 * */
export const expandClusterStyleFunction = (feature: Feature, resolution: number): Style[] => {
    const styles: Style[] = [];
    styles.push(
        new Style({
            image: new Circle({
                radius: feature.get('radius'),
                fill: new Fill({
                    color: 'rgba(255, 153, 0, 0.2)'
                })
            })
        })
    );

    const originalClusterCoord: [number, number] = (<Point>feature.getGeometry()).getCoordinates();
    const originalFeatures = feature.get('features');
    const vectors = expandedClusterTranslationVectors(originalFeatures.length, 40);
    for (let i = originalFeatures.length - 1; i >= 0; --i) {
        const originalFeature: Feature = originalFeatures[i];
        const newCoord: [number, number] = [
            originalClusterCoord[0] + vectors[i].x * resolution,
            originalClusterCoord[1] + vectors[i].y * resolution
        ];

        const style = originalFeature.get('selected')
            ? insSelectedBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature)
            : insBaseStyleFunction(originalFeature.getGeometry().getType(), originalFeature);
        style.setGeometry(new Point(newCoord));
        styles.push(style);

        const lineStyle = insBaseStyleFunction('LineString');
        lineStyle.setGeometry(new LineString([originalClusterCoord, newCoord]));
        styles.push(lineStyle);
    }

    return styles;
};

/**
 * Donne la répartition d'éléments sur un cercle centré sur 0
 * */
export const expandedClusterTranslationVectors = (numberFeatures: number, radius: number): { i: number; x: number; y: number }[] => {
    const coords = [];
    const width = radius * 2;
    let angle, xn, yn, j;
    for (j = 0; j < numberFeatures; j++) {
        angle = (j / (numberFeatures / 2)) * Math.PI; // Calculate the angle at which the element will be placed.
        // For a semicircle, we would use (i / numberFeatures) * Math.PI.
        xn = radius * Math.cos(angle) + width / 2; // Calculate the x position of the element.
        yn = radius * Math.sin(angle) + width / 2; // Calculate the y position of the element.
        coords.push({ i: j, x: xn - radius, y: yn - radius }); // - radius to translate the element to a 0 centered graph
    }
    return coords;
};

export const getMapImageIconUrl = (objectType: string): string => {
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
        case 'geoMarker':
            return IMAGE_URL_GEOMARKER;
        default:
            return null;
    }
};

export const getSelectedImageIconUrl = (objectType: string): string => {
    switch (objectType) {
        case 'geoMarker':
            return IMAGE_URL_SELECTED_GEOMARKER;
        case 'RawData':
            return IMAGE_URL_SELECTED_RAW;
        default:
            return getMapImageIconUrl(objectType);
    }
};
