/**
 * Created by gFolgoas on 16/01/2019.
 */
/* tslint:disable:no-bitwise */
import { IRawData, RawData } from '../model/raw-data.model';
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

/**
 * Return: nouvel array dont l'item est retiré si présent ou ajouté dans le cas contraire
 * */
export const updateUniqueElementArray = (arr: any[], item: any): any[] => {
    const index: number = arr.indexOf(item);
    return index !== -1 ? arr.slice(0, index).concat(arr.slice(index + 1)) : [...arr, item];
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

export const removeDuplicate = (list: any[]): any[] => {
    const temp = {};
    list.forEach(i => (temp[i] = 0));
    return Object.keys(temp);
};

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
    return Object.keys(entity).find(key => /^.*Symbol$/.test(key)) || '';
};

export const getGenericImageProperty = (entity: GenericModel): string => {
    if (entity['entityType'] === 'RawData') {
        return 'rawDataData';
    }
    return Object.keys(entity).find(key => /^.*Image$/.test(key)) || '';
};

export const getGenericNameProperty = (entity: GenericModel): string => {
    return Object.keys(entity).find(key => /^.*Name$/.test(key)) || '';
};

export const getGenericCoordinatesProperty = (entity: GenericModel): string => {
    return Object.keys(entity).find(key => /^.*Coordinates$/.test(key)) || '';
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

/**
 * Les svg doivent être de taille 100x100 px
 * */
export const SYMBOL_URLS = {
    network: {
        IMAGE_URL_BIO: '../../../content/images/icons/hostage.svg',
        IMAGE_URL_SELECTED_BIO: '../../../content/images/icons/hostage-bis.svg',
        IMAGE_URL_EVENT: '../../../content/images/icons/speech.svg',
        IMAGE_URL_SELECTED_EVENT: '../../../content/images/icons/speech-bis.svg',
        IMAGE_URL_EQUIP: '../../../content/images/icons/supply.svg',
        IMAGE_URL_SELECTED_EQUIP: '../../../content/images/icons/supply-bis.svg',
        IMAGE_URL_LOC: '../../../content/images/icons/map.svg',
        IMAGE_URL_SELECTED_LOC: '../../../content/images/icons/map-bis.svg',
        IMAGE_URL_ORGA: '../../../content/images/icons/crowd.svg',
        IMAGE_URL_SELECTED_ORGA: '../../../content/images/icons/crowd-bis.svg',
        IMAGE_URL_DEFAULT: '../../../content/images/icons/flag.svg',
        IMAGE_URL_RAW: '../../../content/images/icons/flag.svg',
        IMAGE_URL_SELECTED_RAW: '../../../content/images/icons/flag-bis.svg'
    },
    map: {
        IMAGE_URL_BIO: '../../../content/images/icons/hostage.svg',
        IMAGE_URL_SELECTED_BIO: '../../../content/images/icons/hostage-bis.svg',
        IMAGE_URL_EVENT: '../../../content/images/icons/speech.svg',
        IMAGE_URL_SELECTED_EVENT: '../../../content/images/icons/speech-bis.svg',
        IMAGE_URL_EQUIP: '../../../content/images/icons/supply.svg',
        IMAGE_URL_SELECTED_EQUIP: '../../../content/images/icons/supply-bis.svg',
        IMAGE_URL_LOC: '../../../content/images/icons/map.svg',
        IMAGE_URL_SELECTED_LOC: '../../../content/images/icons/map-bis.svg',
        IMAGE_URL_ORGA: '../../../content/images/icons/crowd.svg',
        IMAGE_URL_SELECTED_ORGA: '../../../content/images/icons/crowd-bis.svg',
        IMAGE_URL_DEFAULT: '../../../content/images/icons/flag.svg',
        IMAGE_URL_RAW: '../../../content/images/icons/flag.svg',
        IMAGE_URL_SELECTED_RAW: '../../../content/images/icons/flag-bis.svg',
        IMAGE_URL_GEOMARKER: '../../../content/images/geo-marker.svg',
        IMAGE_URL_SELECTED_GEOMARKER: '../../../content/images/geo-marker-selected.svg',

        IMAGE_URL_BIO_BIS: '../../../content/images/icons/hostage-ter.svg',
        IMAGE_URL_EVENT_BIS: '../../../content/images/icons/speech-ter.svg',
        IMAGE_URL_EQUIP_BIS: '../../../content/images/icons/supply-ter.svg',
        IMAGE_URL_LOC_BIS: '../../../content/images/icons/map-ter.svg',
        IMAGE_URL_ORGA_BIS: '../../../content/images/icons/crowd-ter.svg',
        IMAGE_URL_DEFAULT_BIS: '../../../content/images/icons/flag-ter.svg',
        IMAGE_URL_RAW_BIS: '../../../content/images/icons/flag-ter.svg'
    }
};
