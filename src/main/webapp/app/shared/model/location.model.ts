export const enum LocationType {
    CITY = 'CITY',
    COUNTRY = 'COUNTRY',
    REGION = 'REGION',
    ADMINISTRATIVE = 'ADMINISTRATIVE',
    UNKNOWN = 'UNKNOWN'
}

export interface ILocation {
    id?: string;
    locationName?: string;
    locationType?: LocationType;
    locationCoordinates?: string;
    locationImageContentType?: string;
    locationImage?: any;
    locationSymbol?: string;
}

export class Location implements ILocation {
    constructor(
        public id?: string,
        public locationName?: string,
        public locationType?: LocationType,
        public locationCoordinates?: string,
        public locationImageContentType?: string,
        public locationImage?: any,
        public locationSymbol?: string
    ) {}
}
