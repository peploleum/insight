import { IGraphStructureNodeDTO } from './node.model';
/**
 * Created by gFolgoas on 18/01/2019.
 */
export interface IMapDataDTO {
    id?: string;
    externalId?: string;
    label?: string;
    objectType?: string;
    description?: string;
    coordinate?: number[];
    geometry?: any;
    properties?: any;
}

export class MapDataDTO implements IMapDataDTO {
    constructor(
        public id?: string,
        public externalId?: string,
        public label?: string,
        public objectType?: string,
        public description?: string,
        public coordinate?: number[],
        public geometry?: any,
        public properties?: any
    ) {}
}

export class MapSchema {
    hierarchicContent: IGraphStructureNodeDTO[]; // graph centered on the root entity
    flattenContent: {}; // form: {key: nodeId, value: [relationNodeId]}
    flatLevelContent: { rootLevel: {}; firstLevel: {}; secondLevel: {} };

    constructor(hierarchicContent: IGraphStructureNodeDTO[]) {
        this.hierarchicContent = hierarchicContent;
        this.flattenContent = this.updateFlattenContent(this.hierarchicContent, {});
        this.flatLevelContent = this.updateFlatLevelContent(this.hierarchicContent);
    }

    updateFlattenContent(hierarchicContent: IGraphStructureNodeDTO[], flattenContent = {}): {} {
        hierarchicContent.forEach(node => {
            flattenContent[node.nodeId] = node.relations ? node.relations.map(relation => relation.nodeId) : [];
            if (node.relations) {
                this.updateFlattenContent(node.relations, flattenContent);
            }
        });
        return flattenContent;
    }

    /**
     * Flat list d'Ids dédoublonnés par niveau de relation
     * */
    updateFlatLevelContent(hierarchicContent: IGraphStructureNodeDTO[]): { rootLevel: {}; firstLevel: {}; secondLevel: {} } {
        const flatLevelContent = { rootLevel: {}, firstLevel: {}, secondLevel: {} };
        hierarchicContent.forEach(graph => {
            const rootObject = {};
            const firstLevelObject = {};
            const secondLevelObject = {};

            const firstLevelIds: string[] = graph.relations.map(node => node.nodeId);
            const secondLevelIds: string[] = graph.relations
                .filter(node => node.relations && node.relations.length > 0)
                .map(node => node.relations)
                .reduce((x, y) => x.concat(y), [])
                .map(node => node.nodeId);

            rootObject[graph.nodeId] = 0;
            Object.assign(flatLevelContent['rootLevel'], rootObject);

            firstLevelIds.forEach(id => {
                if (!flatLevelContent['rootLevel'].hasOwnProperty(id)) {
                    firstLevelObject[id] = 0;
                }
            });
            Object.assign(flatLevelContent['firstLevel'], firstLevelObject);

            secondLevelIds.forEach(id => {
                if (!flatLevelContent['firstLevel'].hasOwnProperty(id)) {
                    secondLevelObject[id] = 0;
                }
            });
            Object.assign(flatLevelContent['secondLevel'], secondLevelObject);
        });
        return flatLevelContent;
    }

    getEntityLevel(id: number | string): number {
        if (this.flatLevelContent.firstLevel.hasOwnProperty(id)) {
            return 1;
        } else if (this.flatLevelContent.secondLevel.hasOwnProperty(id)) {
            return 2;
        } else {
            return 0;
        }
    }
}
