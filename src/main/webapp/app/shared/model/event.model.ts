import { IEquipment } from 'app/shared/model//equipment.model';
import { ILocation } from 'app/shared/model//location.model';
import { IOrganisation } from 'app/shared/model//organisation.model';
import { IBiographics } from 'app/shared/model//biographics.model';

export const enum EventType {
    POLITICAL = 'POLITICAL',
    DOMESTIC = 'DOMESTIC',
    ACCIDENT = 'ACCIDENT',
    RUMOR = 'RUMOR',
    UNKNOWN = 'UNKNOWN'
}

export interface IEvent {
    id?: number;
    eventName?: string;
    eventDescription?: string;
    eventType?: EventType;
    eventCoordinates?: string;
    equipment?: IEquipment[];
    locations?: ILocation[];
    organisations?: IOrganisation[];
    biographics?: IBiographics[];
}

export class Event implements IEvent {
    constructor(
        public id?: number,
        public eventName?: string,
        public eventDescription?: string,
        public eventType?: EventType,
        public eventCoordinates?: string,
        public equipment?: IEquipment[],
        public locations?: ILocation[],
        public organisations?: IOrganisation[],
        public biographics?: IBiographics[]
    ) {}
}
