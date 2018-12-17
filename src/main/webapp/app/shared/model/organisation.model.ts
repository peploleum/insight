export const enum Size {
    SMALL = 'SMALL',
    MEDIUM = 'MEDIUM',
    LARGE = 'LARGE'
}

export interface IOrganisation {
    id?: string;
    organisationName?: string;
    organisationDescrption?: string;
    organisationSize?: Size;
    organisationCoordinates?: string;
    organisationImageContentType?: string;
    organisationImage?: any;
    organisationSymbol?: string;
}

export class Organisation implements IOrganisation {
    constructor(
        public id?: string,
        public organisationName?: string,
        public organisationDescrption?: string,
        public organisationSize?: Size,
        public organisationCoordinates?: string,
        public organisationImageContentType?: string,
        public organisationImage?: any,
        public organisationSymbol?: string
    ) {}
}
