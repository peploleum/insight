import { Moment } from 'moment';

export interface IReport {
    id?: number;
    nom?: string;
    type?: string;
    libelle?: string;
    objetsReferences?: string;
    datePublication?: Moment;
    linkOfNom?: string;
    linkOfId?: number;
}

export class Report implements IReport {
    constructor(
        public id?: number,
        public nom?: string,
        public type?: string,
        public libelle?: string,
        public objetsReferences?: string,
        public datePublication?: Moment,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
