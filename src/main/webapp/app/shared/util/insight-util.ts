/**
 * Created by gFolgoas on 16/01/2019.
 */
/* tslint:disable:no-bitwise */
import { IRawData, RawData } from '../model/raw-data.model';
import { HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { Biographics } from '../model/biographics.model';
import { Equipment } from '../model/equipment.model';
import { Organisation } from '../model/organisation.model';
import { Event } from '../model/event.model';
import { Location } from '../model/location.model';
import { GenericModel } from '../model/generic.model';
export const UUID = (): string => {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        const r = (Math.random() * 16) | 0,
            v = c === 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
};
/* tslint:enable:no-bitwise */

/**
 * Fix pour une erreur de typescript sur EventTarget (property result manquante)
 * */
export interface FileReaderEventTarget extends EventTarget {
    result: string;
}

export class ToolbarButtonParameters {
    action: string;
    icon: string;
    tooltip: string;
    isToggled?: boolean;
    subButtons?: ToolbarButtonParameters[];

    constructor(action: string, icon: string, tooltip: string, isToggled?: boolean, subButtons?: ToolbarButtonParameters[]) {
        this.action = action;
        this.icon = icon;
        this.tooltip = tooltip;
        this.isToggled = isToggled;
        this.subButtons = subButtons;
    }
}

export type TabTarget = 'MAP' | 'NETWORK' | 'DASHBOARD' | 'CARD' | 'SOURCES';

export class TabContext {
    tabTarget: TabTarget;
    context: Map<string, ContextElement>;

    constructor(tabTarget: TabTarget, context: Map<string, ContextElement>) {
        this.tabTarget = tabTarget;
        this.context = context;
    }
}

export class ContextElement<T = {}> {
    key: string;
    value: T;

    constructor(key: string, value: T) {
        this.key = key;
        this.value = value;
    }
}

export const ENTITY_TYPE_LIST: string[] = ['Biographics', 'Equipment', 'Event', 'Location', 'Organisation', 'RawData'];

export const toKebabCase = (str: string): string => {
    if (str) {
        return str
            .match(/[A-Z]{2,}(?=[A-Z][a-z]+[0-9]*|\b)|[A-Z]?[a-z]+[0-9]*|[A-Z]|[0-9]+/g)
            .map(x => x.toLowerCase())
            .join('-');
    }
    return null;
};

export const convertRawDataDateFromClient = (rawData: IRawData): IRawData => {
    const copy: IRawData = Object.assign({}, rawData, {
        rawDataCreationDate:
            rawData.rawDataCreationDate != null && rawData.rawDataCreationDate.isValid() ? rawData.rawDataCreationDate.toJSON() : null,
        rawDataExtractedDate:
            rawData.rawDataExtractedDate != null && rawData.rawDataExtractedDate.isValid() ? rawData.rawDataExtractedDate.toJSON() : null
    });
    return copy;
};

export const convertRawDataDateFromServer = (rawData: IRawData): IRawData => {
    if (rawData) {
        rawData.rawDataCreationDate = rawData.rawDataCreationDate != null ? moment(rawData.rawDataCreationDate) : null;
        rawData.rawDataExtractedDate = rawData.rawDataExtractedDate != null ? moment(rawData.rawDataExtractedDate) : null;
    }
    return rawData;
};

export const convertRawDataDateArrayFromServer = (rawDataList: IRawData[]): IRawData[] => {
    if (rawDataList) {
        rawDataList.forEach((rawData: IRawData) => {
            rawData.rawDataCreationDate = rawData.rawDataCreationDate != null ? moment(rawData.rawDataCreationDate) : null;
            rawData.rawDataExtractedDate = rawData.rawDataExtractedDate != null ? moment(rawData.rawDataExtractedDate) : null;
        });
    }
    return rawDataList;
};

export class DragParameter {
    data: string;
    event: any;

    constructor(data: string, event: any) {
        this.data = data;
        this.event = event;
    }
}

export const BASE64URI = (base64Content: string, fileType?: string): string => {
    return `data:image/${fileType || 'png'};base64,${base64Content}`;
};

export const getSourceTypeIcon = (sourceType: string): string => {
    switch (sourceType) {
        case 'TWITTER':
            return 'apple-alt';
        case 'RSS':
            return 'rss-square';
        case 'SYSLOG':
            return 'shield-alt';
        default:
            return 'rss-square';
    }
};

export const getDataTypeIcon = (dataType: string): string => {
    switch (dataType) {
        case 'CYBER':
            return 'user-secret';
        case 'OSINT':
            return 'wifi';
        default:
            return 'rss-square';
    }
};

export const assertGenericType = (type: string, entity: GenericModel): boolean => {
    switch (type) {
        case 'Biographics':
            return Object.keys(new Biographics()).every(key => entity.hasOwnProperty(key));
        case 'Equipment':
            return Object.keys(new Equipment()).every(key => entity.hasOwnProperty(key));
        case 'Event':
            return Object.keys(new Event()).every(key => entity.hasOwnProperty(key));
        case 'Location':
            return Object.keys(new Location()).every(key => entity.hasOwnProperty(key));
        case 'Organisation':
            return Object.keys(new Organisation()).every(key => entity.hasOwnProperty(key));
        case 'RawData':
            return Object.keys(new RawData()).every(key => entity.hasOwnProperty(key));
        default:
            return false;
    }
};

export const getGenericSymbolProperty = (entity: GenericModel): string => {
    switch (entity['entityType']) {
        case 'Biographics':
            return 'biographicsSymbol';
        case 'Equipment':
            return 'equipmentSymbol';
        case 'Event':
            return 'eventSymbol';
        case 'Location':
            return 'locationSymbol';
        case 'Organisation':
            return 'organisationSymbol';
        case 'RawData':
            return 'rawDataSymbol';
        default:
            return null;
    }
};

/**
 * Renseigne le type d'un GenericModel
 * */
export const genericTypeResolver = (entity: GenericModel): GenericModel => {
    for (const i of ENTITY_TYPE_LIST) {
        if (assertGenericType(i, entity)) {
            entity['entityType'] = i;
            break;
        }
    }
    return entity;
};
