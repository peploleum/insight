import { IEvent } from 'app/shared/model//event.model';
import { IEquipment } from 'app/shared/model//equipment.model';
import { ILocation } from 'app/shared/model//location.model';
import { IOrganisation } from 'app/shared/model//organisation.model';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    UNKNOWN = 'UNKNOWN'
}

export interface IBiographics {
    id?: number;
    biographicsFirstname?: string;
    biographicsName?: string;
    biographicsAge?: number;
    biographicsGender?: Gender;
    biographicsPhotoContentType?: string;
    biographicsPhoto?: any;
    biographicsCoordinates?: string;
    events?: IEvent[];
    equipment?: IEquipment[];
    locations?: ILocation[];
    organisations?: IOrganisation[];
}

export class Biographics implements IBiographics {
    constructor(
        public id?: number,
        public biographicsFirstname?: string,
        public biographicsName?: string,
        public biographicsAge?: number,
        public biographicsGender?: Gender,
        public biographicsPhotoContentType?: string,
        public biographicsPhoto?: any,
        public biographicsCoordinates?: string,
        public events?: IEvent[],
        public equipment?: IEquipment[],
        public locations?: ILocation[],
        public organisations?: IOrganisation[]
    ) {}
}
