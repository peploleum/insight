import { IBiographics } from 'app/shared/model//biographics.model';
import { IEvent } from 'app/shared/model//event.model';
import { IEquipment } from 'app/shared/model//equipment.model';
import { IOrganisation } from 'app/shared/model//organisation.model';

export const enum LocationType {
    CITY = 'CITY',
    COUNTRY = 'COUNTRY',
    UNKNOWN = 'UNKNOWN'
}

export interface ILocation {
    id?: number;
    locationName?: string;
    locationType?: LocationType;
    locationCoordinates?: string;
    biographics?: IBiographics[];
    events?: IEvent[];
    equipment?: IEquipment[];
    organisations?: IOrganisation[];
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public locationName?: string,
        public locationType?: LocationType,
        public locationCoordinates?: string,
        public biographics?: IBiographics[],
        public events?: IEvent[],
        public equipment?: IEquipment[],
        public organisations?: IOrganisation[]
    ) {}
}
