import { Moment } from 'moment';

export const enum EventType {
    POLITICAL = 'POLITICAL',
    DOMESTIC = 'DOMESTIC',
    ACCIDENT = 'ACCIDENT',
    TERRORIST = 'TERRORIST',
    CRIMINAL = 'CRIMINAL',
    UNKNOWN = 'UNKNOWN'
}

export interface IEvent {
    id?: string;
    eventName?: string;
    eventDescription?: string;
    eventType?: EventType;
    eventDate?: Moment;
    eventCoordinates?: string;
    eventImageContentType?: string;
    eventImage?: any;
    eventSymbol?: string;
    externalId?: string;
}

export class Event implements IEvent {
    constructor(
        public id?: string,
        public eventName?: string,
        public eventDescription?: string,
        public eventType?: EventType,
        public eventDate?: Moment,
        public eventCoordinates?: string,
        public eventImageContentType?: string,
        public eventImage?: any,
        public eventSymbol?: string,
        public externalId?: string
    ) {}
}
