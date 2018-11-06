import { IAttackPattern } from 'app/shared/model//attack-pattern.model';
import { ICampaign } from 'app/shared/model//campaign.model';
import { ICourseOfAction } from 'app/shared/model//course-of-action.model';
import { IActor } from 'app/shared/model//actor.model';
import { IActivityPattern } from 'app/shared/model//activity-pattern.model';
import { IIntrusionSet } from 'app/shared/model//intrusion-set.model';
import { IMalware } from 'app/shared/model//malware.model';
import { IObservedData } from 'app/shared/model//observed-data.model';
import { IReport } from 'app/shared/model//report.model';
import { IThreatActor } from 'app/shared/model//threat-actor.model';
import { ITool } from 'app/shared/model//tool.model';
import { IVulnerability } from 'app/shared/model//vulnerability.model';

export interface INetLink {
    id?: number;
    description?: string;
    nom?: string;
    type?: string;
    level?: string;
    isLinkOfAttackPatterns?: IAttackPattern[];
    isLinkOfCampaigns?: ICampaign[];
    isLinkOfCourseOfActions?: ICourseOfAction[];
    isLinkOfActors?: IActor[];
    isLinkOfActivityPatterns?: IActivityPattern[];
    isLinkOfIntrusionSets?: IIntrusionSet[];
    isLinkOfMalwares?: IMalware[];
    isLinkOfObservedData?: IObservedData[];
    isLinkOfReports?: IReport[];
    isLinkOfThreatActors?: IThreatActor[];
    isLinkOfTools?: ITool[];
    isLinkOfVulnerabilities?: IVulnerability[];
}

export class NetLink implements INetLink {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public type?: string,
        public level?: string,
        public isLinkOfAttackPatterns?: IAttackPattern[],
        public isLinkOfCampaigns?: ICampaign[],
        public isLinkOfCourseOfActions?: ICourseOfAction[],
        public isLinkOfActors?: IActor[],
        public isLinkOfActivityPatterns?: IActivityPattern[],
        public isLinkOfIntrusionSets?: IIntrusionSet[],
        public isLinkOfMalwares?: IMalware[],
        public isLinkOfObservedData?: IObservedData[],
        public isLinkOfReports?: IReport[],
        public isLinkOfThreatActors?: IThreatActor[],
        public isLinkOfTools?: ITool[],
        public isLinkOfVulnerabilities?: IVulnerability[]
    ) {}
}
