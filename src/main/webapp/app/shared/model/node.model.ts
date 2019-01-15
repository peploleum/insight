export interface INodeDTO {
    id?: string;
    label?: string;
    type?: string;
}

export class NodeDTO implements INodeDTO {
    constructor(public id?: string, public label?: string, public type?: string) {}
}
