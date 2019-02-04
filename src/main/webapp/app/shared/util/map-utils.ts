/**
 * Created by gFolgoas on 22/01/2019.
 */
import { IRawData } from '../model/raw-data.model';
import Style from 'ol/style/style';
import Stroke from 'ol/style/stroke';
import Fill from 'ol/style/fill';

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

export const dessinStyle = (strokeClr: string, fillClr: string, linedash: number, strokeWdth: number): Style => {
    return new Style({
        stroke: new Stroke({
            color: strokeClr,
            lineDash: [linedash],
            width: strokeWdth
        }),
        fill: new Fill({
            color: fillClr
        })
    });
};

export class DrawingLayer {
    layerId: string;
    layerName: string;
    selected: boolean;

    constructor(layerId: string, layerName: string, selected: boolean) {
        this.layerId = layerId;
        this.layerName = layerName;
        this.selected = selected;
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
