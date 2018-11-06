import { IVulnerability } from 'app/shared/model//vulnerability.model';

export interface ITool {
    id?: number;
    nom?: string;
    type?: string;
    libelle?: string;
    description?: string;
    version?: string;
    isTargetsToolToVulnerabilities?: IVulnerability[];
    usesToolToIntrusionSetNom?: string;
    usesToolToIntrusionSetId?: number;
    isUsesToolToMalwareNom?: string;
    isUsesToolToMalwareId?: number;
    linkOfNom?: string;
    linkOfId?: number;
    usesToolToThreatActorNom?: string;
    usesToolToThreatActorId?: number;
}

export class Tool implements ITool {
    constructor(
        public id?: number,
        public nom?: string,
        public type?: string,
        public libelle?: string,
        public description?: string,
        public version?: string,
        public isTargetsToolToVulnerabilities?: IVulnerability[],
        public usesToolToIntrusionSetNom?: string,
        public usesToolToIntrusionSetId?: number,
        public isUsesToolToMalwareNom?: string,
        public isUsesToolToMalwareId?: number,
        public linkOfNom?: string,
        public linkOfId?: number,
        public usesToolToThreatActorNom?: string,
        public usesToolToThreatActorId?: number
    ) {}
}
