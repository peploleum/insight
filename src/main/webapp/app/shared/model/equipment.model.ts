import { ILocation } from 'app/shared/model//location.model';
import { IOrganisation } from 'app/shared/model//organisation.model';
import { IBiographics } from 'app/shared/model//biographics.model';
import { IEvent } from 'app/shared/model//event.model';

export const enum EquipmentType {
    WEAPON = 'WEAPON',
    TOOL = 'TOOL',
    GARDENING = 'GARDENING',
    RUMOR = 'RUMOR',
    UNKNOWN = 'UNKNOWN'
}

export interface IEquipment {
    id?: number;
    equipmentName?: string;
    equipmentDescription?: string;
    equipmentType?: EquipmentType;
    equipmentCoordinates?: string;
    locations?: ILocation[];
    organisations?: IOrganisation[];
    biographics?: IBiographics[];
    events?: IEvent[];
}

export class Equipment implements IEquipment {
    constructor(
        public id?: number,
        public equipmentName?: string,
        public equipmentDescription?: string,
        public equipmentType?: EquipmentType,
        public equipmentCoordinates?: string,
        public locations?: ILocation[],
        public organisations?: IOrganisation[],
        public biographics?: IBiographics[],
        public events?: IEvent[]
    ) {}
}
