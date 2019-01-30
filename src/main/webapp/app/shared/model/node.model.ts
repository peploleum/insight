import { DataSet, Edge, IdType, Node } from 'vis';
export interface IGraphyNodeDTO {
    id?: string;
    mongoId?: string;
    label?: string;
    type?: string;
    title?: string;
    to?: IGraphyRelationDTO[];
}

/**
 * @Param: id: string
 * @Param: mongoId: string
 * @Param: label: string, label principal du node
 * @Param: type: string, type d'objet
 * @Param: title: string, légende optionnelle du node (affichée au onHover par exemple)
 * @Param: to: IGraphyRelationDTO[], relations vers les objets cibles (relation directe pas inverse)
 * */
export class GraphyNodeDTO implements IGraphyNodeDTO {
    constructor(
        public id?: string,
        mongoId?: string,
        public label?: string,
        public type?: string,
        public title?: string,
        to?: IGraphyRelationDTO[]
    ) {}
}

/**
 * @Param: id: string, Id de l'objet cible
 * @Param: mongoId: string, MongoId de l'objet cible
 * @Param: label: string, label de la relation
 * */
export interface IGraphyRelationDTO {
    id?: string;
    mongoId?: string;
    label?: string;
}

export class GraphyRelationDTO implements IGraphyRelationDTO {
    constructor(public id?: string, mongoId?: string, public label?: string) {}
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
