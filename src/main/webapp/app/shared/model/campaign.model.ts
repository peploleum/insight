export interface ICampaign {
    id?: number;
    description?: string;
    nom?: string;
    objectif?: string;
    alias?: string;
    type?: string;
    linkOfNom?: string;
    linkOfId?: number;
}

export class Campaign implements ICampaign {
    constructor(
        public id?: number,
        public description?: string,
        public nom?: string,
        public objectif?: string,
        public alias?: string,
        public type?: string,
        public linkOfNom?: string,
        public linkOfId?: number
    ) {}
}
