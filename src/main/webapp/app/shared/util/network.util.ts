/**
 * Created by gFolgoas on 22/02/2019.
 */
import { Edge, IdType, Node } from 'vis';
import { EdgeDTO, GraphDataSet } from '../model/node.model';
export class NetworkState {
    LAYOUT_DIR: string;
    LAYOUT_FREE: boolean;
    PHYSICS_ENABLED: boolean;
    ENTITIES_FILTER: string[];
    SORT_METHOD?: string;
    ADD_NEIGHBOURS?: boolean;
    CLUSTER_NODES?: boolean;
    AUTO_REFRESH?: boolean;
    DISPLAY_RELATION?: boolean;
    CREATE_RELATIONS?: boolean;

    constructor(
        LAYOUT_DIR: string,
        LAYOUT_FREE: boolean,
        PHYSICS_ENABLED: boolean,
        ENTITIES_FILTER: string[],
        SORT_METHOD?: string,
        ADD_NEIGHBOURS?: boolean,
        CLUSTER_NODES?: boolean,
        AUTO_REFRESH?: boolean,
        DISPLAY_RELATION?: boolean,
        CREATE_RELATIONS?: boolean
    ) {
        this.LAYOUT_DIR = LAYOUT_DIR;
        this.LAYOUT_FREE = LAYOUT_FREE;
        this.PHYSICS_ENABLED = PHYSICS_ENABLED;
        this.ENTITIES_FILTER = ENTITIES_FILTER;
        this.SORT_METHOD = SORT_METHOD;
        this.ADD_NEIGHBOURS = ADD_NEIGHBOURS;
        this.CLUSTER_NODES = CLUSTER_NODES;
        this.AUTO_REFRESH = AUTO_REFRESH;
        this.DISPLAY_RELATION = DISPLAY_RELATION;
        this.CREATE_RELATIONS = CREATE_RELATIONS;
    }
}

export class DataContentInfo {
    label: string;
    id: IdType;
    idMongo: string;
    objectType: string;
    title?: string;
    image?: string;

    constructor(label: string, id: IdType, idMongo: string, objectType: string, title?: string, image?: string) {
        this.label = label;
        this.id = id;
        this.idMongo = idMongo;
        this.objectType = objectType;
        this.title = title;
        this.image = image;
    }
}

export class NetworkSymbol {
    symbolId?: string;
    base64?: string;
    isPresentInNetwork: boolean;

    constructor(symbolId?: string, base64?: string, isPresentInNetwork?: boolean) {
        this.symbolId = symbolId;
        this.base64 = base64;
        this.isPresentInNetwork = isPresentInNetwork;
    }
}

/**
 * Dédoublonnage avant ajout au network
 * On évite de remplacer toute la liste de nodes du network pour ne pas perdre
 * les positions de ceux déjà présents.
 * */
export const addNodes = (dataSet: GraphDataSet, nodes: Node[], edges: Edge[]) => {
    const tempN = {};
    const tempAddN = {};
    dataSet.nodes.forEach(n => (tempN[n.id] = n));
    nodes.forEach(node => {
        if (!tempN[node.id]) {
            tempAddN[node.id] = node;
        }
    });
    nodes = Object.keys(tempAddN).map(key => tempAddN[key]);

    const tempE = {};
    const tempAddE: Edge[] = [];
    dataSet.edges.forEach(e => {
        const tempEdges: Edge[] = tempE[e.from] || [];
        tempEdges.push(e);
        tempE[e.from] = tempEdges;
    });
    edges.forEach((edge: EdgeDTO) => {
        const tempEdges: EdgeDTO[] = tempE[edge.from] || [];
        let isPresent = false;
        for (let i = 0; i < tempEdges.length; i++) {
            const e: EdgeDTO = tempEdges[i];
            if (e.from === edge.from && e.to === edge.to) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            tempEdges.push(edge);
            tempE[edge.from] = tempEdges;
            tempAddE.push(edge);
        }
    });
    edges = Object.keys(tempAddE).map(key => tempAddE[key]);

    dataSet.nodes.add(nodes);
    dataSet.edges.add(edges);
};
