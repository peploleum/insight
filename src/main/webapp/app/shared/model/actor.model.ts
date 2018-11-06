import { IIntrusionSet } from 'app/shared/model//intrusion-set.model';
import { IMalware } from 'app/shared/model//malware.model';

export interface IActor {
    id?: number;
    description?: string;
    nom?: string;
    type?: string;
    libelle?: string;
    classeIdentite?: string;
    targetsActorToIntrusionSets?: IIntrusionSet[];
    targetsActorToMalwares?: IMalware[];
    linkOfNom?: string;
    linkOfId?: number;
}

export class Actor implements IActor {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public type?: string,
        public libelle?: string,
        public classeIdentite?: string,
        public targetsActorToIntrusionSets?: IIntrusionSet[],
        public targetsActorToMalwares?: IMalware[],
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
