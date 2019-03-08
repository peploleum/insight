import { Moment } from 'moment';

export interface IRawData {
    id?: string;
    rawDataName?: string;
    rawDataType?: string;
    rawDataSubType?: string;
    rawDataSourceName?: string;
    rawDataSourceUri?: string;
    rawDataSourceType?: string;
    rawDataContent?: string;
    rawDataCreationDate?: Moment;
    rawDataExtractedDate?: Moment;
    rawDataSymbol?: string;
    rawDataDataContentType?: string;
    rawDataData?: any;
    rawDataCoordinates?: string;
    rawDataAnnotations?: string;
    externalId?: string;
}

export class RawData implements IRawData {
    constructor(
        public id?: string,
        public rawDataName?: string,
        public rawDataType?: string,
        public rawDataSubType?: string,
        public rawDataSourceName?: string,
        public rawDataSourceUri?: string,
        public rawDataSourceType?: string,
        public rawDataContent?: string,
        public rawDataCreationDate?: Moment,
        public rawDataExtractedDate?: Moment,
        public rawDataSymbol?: string,
        public rawDataDataContentType?: string,
        public rawDataData?: any,
        public rawDataCoordinates?: string,
        public rawDataAnnotations?: string,
        public externalId?: string
    ) {}
}

export class EntityPosition {
    constructor(
        public idMongo: string,
        public idJanus: string,
        public position: number,
        public entityWord: string,
        public entityType: string
    ) {}
}
