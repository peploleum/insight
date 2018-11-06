import { IVulnerability } from 'app/shared/model//vulnerability.model';
import { ITool } from 'app/shared/model//tool.model';

export interface IThreatActor {
    id?: number;
    nom?: string;
    type?: string;
    libelle?: string;
    specification?: string;
    role?: string;
    isTargetsThreatActorToVulnerabilities?: IVulnerability[];
    isUsesThreatActorToTools?: ITool[];
    isUsesThreatActorToMalwareNom?: string;
    isUsesThreatActorToMalwareId?: number;
    linkOfNom?: string;
    linkOfId?: number;
}

export class ThreatActor implements IThreatActor {
    constructor(
        public id?: number,
        public nom?: string,
        public type?: string,
        public libelle?: string,
        public specification?: string,
        public role?: string,
        public isTargetsThreatActorToVulnerabilities?: IVulnerability[],
        public isUsesThreatActorToTools?: ITool[],
        public isUsesThreatActorToMalwareNom?: string,
        public isUsesThreatActorToMalwareId?: number,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
