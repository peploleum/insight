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

export type MapLayerType = 'DESSIN' | 'KML' | 'SOURCE';
export type MapLayerStatus = 'NEW' | 'UPDATE' | 'DELETE';

export class MapLayer {
    layerId: string;
    layerName: string;
    layerType: MapLayerType;
    selected: boolean;
    layerStatus: MapLayerStatus;

    constructor(layerId: string, layerName: string, layerType: MapLayerType, selected: boolean) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.layerType = layerType;
        this.selected = selected;
        this.layerStatus = 'NEW';
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
