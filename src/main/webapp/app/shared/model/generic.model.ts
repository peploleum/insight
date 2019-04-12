/**
 * Created by gFolgoas on 27/02/2019.
 */
import { IBiographics } from './biographics.model';
import { IEquipment } from './equipment.model';
import { IEvent } from './event.model';
import { ILocation } from './location.model';
import { IOrganisation } from './organisation.model';
import { IRawData } from './raw-data.model';
export interface IGenericModel {
    entityType?: string;
}

export class GenericModel implements IBiographics, IEquipment, IEvent, ILocation, IOrganisation, IRawData, IGenericModel {
    constructor() {}
}

export const COORDINATE_FIELD = [
    'biographicsCoordinates',
    'equipmentCoordinates',
    'eventCoordinates',
    'locationCoordinates',
    'organisationCoordinates',
    'rawDataCoordinates'
];
export const NAME_FIELD = ['biographicsName', 'equipmentName', 'eventName', 'locationName', 'organisationName', 'rawDataName'];
export const SYMBOL_FIELD = [
    'biographicsSymbol',
    'equipmentSymbol',
    'eventSymbol',
    'locationSymbol',
    'organisationSymbol',
    'rawDataSymbol'
];
export const CONTENT_FIELD = ['equipmentDescription', 'eventDescription', 'organisationDescrption', 'rawDataContent'];
