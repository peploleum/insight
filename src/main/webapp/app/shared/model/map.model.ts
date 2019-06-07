import { IGraphStructureNodeDTO, IMapSchemaDescriptor, MapSchemaDescriptor } from './node.model';
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
    hierarchicContent: IGraphStructureNodeDTO; // graph centered on the root entity
    flattenContent: {}; // form: {key: nodeId, value: [relationNodeId]} => nodeId = mongoId en carto!!!
    flatLevelContent: { rootLevel: {}; firstLevel: {}; secondLevel: {} };
    schemaDescriptor?: IMapSchemaDescriptor;

    constructor(hierarchicContent: IGraphStructureNodeDTO, schemaDescriptor?: IMapSchemaDescriptor) {
        this.hierarchicContent = hierarchicContent;
        this.flattenContent = this.updateFlattenContent(this.hierarchicContent, {});
        this.flatLevelContent = this.updateFlatLevelContent(this.hierarchicContent);
        if (!schemaDescriptor) {
            const maxDepth = Object.keys(this.flatLevelContent.secondLevel).length > 0 ? 2 : 1;
            const numberEntities = Object.keys(this.flattenContent).length;
            this.schemaDescriptor = new MapSchemaDescriptor(maxDepth, numberEntities);
        }
    }

    updateFlattenContent(hierarchicContent: IGraphStructureNodeDTO, flattenContent = {}): {} {
        flattenContent[hierarchicContent.nodeId] = hierarchicContent.relations
            ? hierarchicContent.relations.map(relation => relation.nodeId)
            : [];
        if (hierarchicContent.relations) {
            hierarchicContent.relations.forEach(node => {
                this.updateFlattenContent(node, flattenContent);
            });
        }
        return flattenContent;
    }

    /**
     * Flat list d'Ids dédoublonnés par niveau de relation
     * */
    updateFlatLevelContent(graph: IGraphStructureNodeDTO): { rootLevel: {}; firstLevel: {}; secondLevel: {} } {
        const flatLevelContent = { rootLevel: {}, firstLevel: {}, secondLevel: {} };

        const firstLevelIds: string[] = graph.relations.map(node => node.nodeId);
        const secondLevelIds: string[] = graph.relations
            .filter(node => node.relations && node.relations.length > 0)
            .map(node => node.relations)
            .reduce((x, y) => x.concat(y), [])
            .map(node => node.nodeId);

        flatLevelContent['rootLevel'][graph.nodeId] = 0;
        firstLevelIds.forEach(id => {
            if (!flatLevelContent['rootLevel'].hasOwnProperty(id)) {
                flatLevelContent['firstLevel'][id] = 0;
            }
        });
        secondLevelIds.forEach(id => {
            if (!flatLevelContent['firstLevel'].hasOwnProperty(id) && !flatLevelContent['rootLevel'].hasOwnProperty(id)) {
                flatLevelContent['secondLevel'][id] = 0;
            }
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
