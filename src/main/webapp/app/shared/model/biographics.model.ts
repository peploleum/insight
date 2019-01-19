export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE',
    UNKNOWN = 'UNKNOWN'
}

export interface IBiographics {
    id?: string;
    biographicsFirstname?: string;
    biographicsName?: string;
    biographicsAge?: number;
    biographicsGender?: Gender;
    biographicsImageContentType?: string;
    biographicsImage?: any;
    biographicsCoordinates?: string;
    biographicsSymbol?: string;
    externalId?: string;
}

export class Biographics implements IBiographics {
    constructor(
        public id?: string,
        public biographicsFirstname?: string,
        public biographicsName?: string,
        public biographicsAge?: number,
        public biographicsGender?: Gender,
        public biographicsImageContentType?: string,
        public biographicsImage?: any,
        public biographicsCoordinates?: string,
        public biographicsSymbol?: string,
        public externalId?: string
    ) {}
}
