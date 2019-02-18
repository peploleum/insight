/**
 * Created by gFolgoas on 22/01/2019.
 */
import { IRawData } from '../model/raw-data.model';

export class MapState {
    DISPLAY_LABEL: boolean;
    DISPLAY_CONTENT_ON_HOVER: boolean;
    FILTER_TYPE: string;
    DESSIN_ENABLED: boolean;

    constructor(DISPLAY_LABEL: boolean, DISPLAY_CONTENT_ON_HOVER: boolean, FILTER_TYPE: string, DESSIN_ENABLED: boolean) {
        this.DISPLAY_LABEL = DISPLAY_LABEL;
        this.DISPLAY_CONTENT_ON_HOVER = DISPLAY_CONTENT_ON_HOVER;
        this.FILTER_TYPE = FILTER_TYPE;
        this.DESSIN_ENABLED = DESSIN_ENABLED;
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
