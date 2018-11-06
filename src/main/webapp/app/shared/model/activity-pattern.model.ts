import { Moment } from 'moment';

export interface IActivityPattern {
    id?: number;
    modele?: string;
    nom?: string;
    type?: string;
    valideAPartirDe?: Moment;
    linkOfNom?: string;
    linkOfId?: number;
}

export class ActivityPattern implements IActivityPattern {
    constructor(
        public id?: number,
        public modele?: string,
        public nom?: string,
        public type?: string,
        public valideAPartirDe?: Moment,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
