import { Moment } from 'moment';

export interface IObservedData {
    id?: number;
    type?: string;
    objetsObserves?: string;
    dateDebut?: Moment;
    dateFin?: Moment;
    nombreJours?: number;
    linkOfNom?: string;
    linkOfId?: number;
}

export class ObservedData implements IObservedData {
    constructor(
        public id?: number,
        public type?: string,
        public objetsObserves?: string,
        public dateDebut?: Moment,
        public dateFin?: Moment,
        public nombreJours?: number,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
