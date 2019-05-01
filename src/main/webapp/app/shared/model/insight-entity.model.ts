export interface IInsightEntity {
    id?: string;
    entityType?: string;
    geometry?: any;
}

export class InsightEntity implements IInsightEntity {
    constructor(public id?: string, entityType?: string, geometry?: any) {}
}
