import { DataSet, Edge, IdType, Node } from 'vis';
export interface IGraphyNodeDTO {
    id?: string;
    mongoId?: string;
    label?: string;
    type?: string;
    title?: string;
}

export class GraphyNodeDTO implements IGraphyNodeDTO {
    constructor(public id?: string, mongoId?: string, public label?: string, public type?: string, public title?: string) {}
}

export class NodeDTO implements Node {
    constructor(
        public label?: string,
        public id?: IdType,
        public mongoId?: string,
        public title?: string,
        public image?: string,
        public color?: any,
        public border?: number,
        public font?: any
    ) {}
}

export class EdgeDTO implements Edge {
    constructor(public from: IdType, public to: IdType) {}
}

export class GraphDataCollection {
    nodes: NodeDTO[];
    edges: EdgeDTO[];

    constructor(nodes: NodeDTO[], edges: EdgeDTO[]) {
        this.nodes = nodes;
        this.edges = edges;
    }
}

export class GraphDataSet {
    nodes: DataSet<Node>;
    edges: DataSet<Edge>;

    constructor(nodes: DataSet<Node>, edges: DataSet<Edge>) {
        this.nodes = nodes;
        this.edges = edges;
    }
}
