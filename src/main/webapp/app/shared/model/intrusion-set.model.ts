import { ITool } from 'app/shared/model//tool.model';

export interface IIntrusionSet {
    id?: number;
    description?: string;
    nom?: string;
    type?: string;
    objectif?: string;
    niveauRessource?: string;
    isUsesIntrusionSetToTools?: ITool[];
    isTargetsIntrusionSetToActorNom?: string;
    isTargetsIntrusionSetToActorId?: number;
    linkOfNom?: string;
    linkOfId?: number;
}

export class IntrusionSet implements IIntrusionSet {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public type?: string,
        public objectif?: string,
        public niveauRessource?: string,
        public isUsesIntrusionSetToTools?: ITool[],
        public isTargetsIntrusionSetToActorNom?: string,
        public isTargetsIntrusionSetToActorId?: number,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
