import { IMalware } from 'app/shared/model//malware.model';

export interface IAttackPattern {
    id?: number;
    description?: string;
    nom?: string;
    referenceExterne?: string;
    tueurProcessus?: string;
    type?: string;
    usesAttackPatternToMalwares?: IMalware[];
    linkOfNom?: string;
    linkOfId?: number;
}

export class AttackPattern implements IAttackPattern {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public referenceExterne?: string,
        public tueurProcessus?: string,
        public type?: string,
        public usesAttackPatternToMalwares?: IMalware[],
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
