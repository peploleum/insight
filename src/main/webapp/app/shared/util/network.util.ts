/**
 * Created by gFolgoas on 22/02/2019.
 */
export class NetworkState {
    LAYOUT_DIR: string;
    LAYOUT_FREE: boolean;
    PHYSICS_ENABLED: boolean;
    ADD_NEIGHBOURS?: boolean;
    CLUSTER_NODES?: boolean;
    AUTO_REFRESH?: boolean;

    constructor(
        LAYOUT_DIR: string,
        LAYOUT_FREE: boolean,
        PHYSICS_ENABLED: boolean,
        ADD_NEIGHBOURS?: boolean,
        CLUSTER_NODES?: boolean,
        AUTO_REFRESH?: boolean
    ) {
        this.LAYOUT_DIR = LAYOUT_DIR;
        this.LAYOUT_FREE = LAYOUT_FREE;
        this.PHYSICS_ENABLED = PHYSICS_ENABLED;
        this.ADD_NEIGHBOURS = ADD_NEIGHBOURS;
        this.CLUSTER_NODES = CLUSTER_NODES;
        this.AUTO_REFRESH = AUTO_REFRESH;
    }
}
