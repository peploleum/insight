import { ILocation } from 'app/shared/model//location.model';
import { IBiographics } from 'app/shared/model//biographics.model';
import { IEvent } from 'app/shared/model//event.model';
import { IEquipment } from 'app/shared/model//equipment.model';

export const enum Size {
    SMALL = 'SMALL',
    MEDIUM = 'MEDIUM',
    LARGE = 'LARGE'
}

export interface IOrganisation {
    id?: number;
    organisationName?: string;
    organisationSize?: Size;
    organisationCoordinates?: string;
    locations?: ILocation[];
    biographics?: IBiographics[];
    events?: IEvent[];
    equipment?: IEquipment[];
}

export class Organisation implements IOrganisation {
    constructor(
        public id?: number,
        public organisationName?: string,
        public organisationSize?: Size,
        public organisationCoordinates?: string,
        public locations?: ILocation[],
        public biographics?: IBiographics[],
        public events?: IEvent[],
        public equipment?: IEquipment[]
    ) {}
}
