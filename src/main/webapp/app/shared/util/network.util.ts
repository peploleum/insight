/**
 * Created by gFolgoas on 22/02/2019.
 */
import { IdType } from 'vis';
export class NetworkState {
    LAYOUT_DIR: string;
    LAYOUT_FREE: boolean;
    PHYSICS_ENABLED: boolean;
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
