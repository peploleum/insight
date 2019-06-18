import { AfterContentInit, AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSet, Edge, IdType, Network, Node, Options } from 'vis';
import { NetworkService } from './network.service';
import { Subscription } from 'rxjs/index';
import { ActivatedRoute } from '@angular/router';
import { EdgeDTO, GraphDataCollection, GraphDataSet, IGraphyNodeDTO, NodeDTO } from 'app/shared/model/node.model';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import {
    ContextElement,
    DragParameter,
    FileReaderEventTarget,
    getGenericSymbolProperty,
    updateUniqueElementArray
} from '../shared/util/insight-util';
import { SideMediatorService } from '../side/side-mediator.service';
import { addNodes, DataContentInfo, NetworkState } from '../shared/util/network.util';
import { ToolbarState } from '../shared/util/side.util';
import { DEBUG_INFO_ENABLED } from '../app.constants';
import { GenericModel } from '../shared/model/generic.model';
import { UserContextService } from '../account/user-context/user-context.service';

@Component({
    selector: 'ins-network',
    templateUrl: './network.component.html',
    styles: [':host { flex-grow: 1}']
})
export class NetworkComponent implements OnInit, AfterViewInit, AfterContentInit, OnDestroy {
    @ViewChild('network', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;

    networkState: NetworkState;

    graphDataSubscription: Subscription;
    actionClickedSubs: Subscription;
    networkStateSubs: Subscription;
    dataSelectedSubs: Subscription;

    constructor(
        private _ns: NetworkService,
        private activatedRoute: ActivatedRoute,
        private _sms: SideMediatorService,
        private _ucs: UserContextService
    ) {}

    ngOnInit() {
        this._ns.JSONFileSelected.subscribe(
            (file: File) => {
                this.addJSONFileElements(file);
                console.log('JSON File Import Successful');
            },
            error => {
                console.log("Erreur lors de l'import de fichier JSON");
            }
        );
    }

    ngAfterViewInit() {
        this.initNetwork();
        this.initNetworkEventListener();

        const dataCtx: ContextElement = this._ucs.getTabContext('NETWORK').context.get('NETWORK_DATA');
        if (dataCtx && dataCtx.value) {
            const ctxDataSet = <GraphDataSet>dataCtx.value;
            this.networkData.nodes.add(ctxDataSet.nodes.get());
            this.networkData.edges.add(ctxDataSet.edges.get());
            this.clusterNodes();
        }

        this.activatedRoute.data.subscribe(({ originNode }) => {
            if (originNode) {
                const data: GenericModel = <GenericModel>originNode;
                if (data['externalId'] != null) {
                    this._ns
                        .getNodeProperties(data['externalId'])
                        .pipe(
                            filter((response: HttpResponse<IGraphyNodeDTO>) => response.ok),
                            map((response: HttpResponse<IGraphyNodeDTO>) => {
                                const rawData: IGraphyNodeDTO = response.body;
                                return NetworkService.getNodeDto(
                                    rawData.label,
                                    rawData.type,
                                    rawData.id,
                                    rawData.idMongo,
                                    '',
                                    rawData.symbole,
                                    this._ns.isHidden(rawData.type).hidden,
                                    this._ns.isHidden(rawData.type).physics
                                );
                            })
                        )
                        .subscribe((nodeDTO: NodeDTO) => {
                            this.addNodes([nodeDTO], []);
                            this.getNodesNeighbours([nodeDTO.id]);
                        });
                    return;
                }
            }
            if (DEBUG_INFO_ENABLED) {
                // this.getMockData();
            }
        });

        this.networkStateSubs = this._ns.networkState.subscribe(state => {
            if (this.networkState) {
                let reloadFiltering = !!this.networkState.ENTITIES_FILTER.find(entity => state.ENTITIES_FILTER.indexOf(entity) === -1);
                reloadFiltering = reloadFiltering || this.networkState.ENTITIES_FILTER.length !== state.ENTITIES_FILTER.length;
                if (reloadFiltering) {
                    this.updateGraphNodeFiltering();
                }
            }
            this.networkState = Object.assign({}, state);
            const updatedEventThreadToolbar = this._ns.getUpdatedEventThreadToolbar();
            this._sms.updateToolbarState(new ToolbarState('EVENT_THREAD', updatedEventThreadToolbar));
        });
        this.actionClickedSubs = this._sms._onNewActionClicked.subscribe(action => {
            this.onActionReceived(action);
        });
        this.dataSelectedSubs = this._sms._onNewDataSelected.subscribe((selectedMongoIds: string[]) => {
            const selectedNodeIds: IdType[] = this.getNodeIdsFromMongoIds(selectedMongoIds);
            this.network.selectNodes(selectedNodeIds, true);
            this._sms._selectedData.next(selectedMongoIds);
        });
    }

    ngOnDestroy() {
        this._ucs.updateContext('NETWORK', new ContextElement<GraphDataSet>('NETWORK_DATA', this.networkData));
        if (this.actionClickedSubs) {
            this.actionClickedSubs.unsubscribe();
        }
        if (this.graphDataSubscription) {
            this.graphDataSubscription.unsubscribe();
        }
        if (this.networkStateSubs) {
            this.networkStateSubs.unsubscribe();
        }
        if (this.dataSelectedSubs) {
            this.dataSelectedSubs.unsubscribe();
        }
        // Clean le behaviorSubject de relations
        this._ns.neighborsEmitter.next(new GraphDataCollection([], []));
    }

    getMockData() {
        this.graphDataSubscription = this._ns.getMockGraphData().subscribe((data: GraphDataCollection) => {
            this.addNodes(data.nodes, data.edges);
        });
    }

    ngAfterContentInit() {}

    getInitialNetworkOption(): Options {
        return {
            layout: this.getLayoutOption(),
            physics: this.getPhysicsOption(),
            interaction: {
                hideEdgesOnDrag: true,
                hover: true,
                hoverConnectedEdges: true,
                keyboard: {
                    enabled: true
                },
                multiselect: true,
                navigationButtons: true
            },
            nodes: {
                shape: 'circularImage'
            }
        };
    }

    getPhysicsOption(): any {
        return {
            enabled: this.getState().PHYSICS_ENABLED,
            solver: 'repulsion'
        };
    }

    getLayoutOption(): any {
        return {
            hierarchical: {
                enabled: !this.getState().LAYOUT_FREE,
                levelSeparation: 130,
                direction: this.getState().LAYOUT_DIR,
                sortMethod: this.getState().SORT_METHOD
            }
        };
    }

    initNetwork() {
        this.networkData = new GraphDataSet(new DataSet(), new DataSet());
        if (this._networkRef && !this.network) {
            this.network = new Network(this._networkRef.nativeElement, this.networkData, this.getInitialNetworkOption());
        }
    }

    initNetworkEventListener() {
        this.network.on('selectNode', properties => {
            const selectedNodeIds: IdType[] = [];
            const selectedEdgeIds: IdType[] = properties.edges.filter(id => !/^cluster/.test(id));
            const selectedClusterIds: IdType[] = [];
            (<IdType[]>properties.nodes).forEach((id: IdType) => {
                if (this.network.isCluster(id)) {
                    selectedClusterIds.push(id);
                } else {
                    selectedNodeIds.push(id);
                }
            });
            if (this.getState().ADD_NEIGHBOURS) {
                this.getNodesNeighbours(selectedNodeIds);
            }
            if (this.getState().CLUSTER_NODES) {
                selectedClusterIds.forEach(id => this.network.openCluster(id));
            }
            this._sms._selectedData.next(this.getMongoIdsFromNodeIds(selectedNodeIds));
            this.emitNeighborsOnSelection(selectedNodeIds, selectedEdgeIds);
        });
        this.network.on('deselectNode', properties => {
            const unselectedNodeIds: IdType[] = [];
            (<IdType[]>properties.nodes).forEach((id: IdType) => {
                if (!this.network.isCluster(id)) {
                    unselectedNodeIds.push(id);
                }
            });
            this._sms._selectedData.next(this.getMongoIdsFromNodeIds(unselectedNodeIds));
        });
        this.networkData.nodes.on('add', (event, properties) => {
            this.updateDataContent();
        });
        this.networkData.nodes.on('remove', (event, properties) => {
            this.updateDataContent();
        });
    }

    getMongoIdsFromNodeIds(nodeIds: IdType[]): string[] {
        const nodes: Node[] = this.networkData.nodes.get(nodeIds);
        return nodes.map(n => (<NodeDTO>n).mongoId);
    }

    getNodeIdsFromMongoIds(mongoIds: string[]): IdType[] {
        return this.networkData.nodes
            .get({
                filter: node => {
                    return mongoIds.indexOf((<NodeDTO>node).mongoId) !== -1;
                }
            })
            .map(node => node.id);
    }

    emitNeighborsOnSelection(nodeIds: IdType[], edgeIds: IdType[]) {
        if (!edgeIds || edgeIds.length === 0) {
            return;
        }
        const edgeArray: Edge[] = this.networkData.edges.get(edgeIds);
        const unduplicatedNodeArrayIds = {};
        edgeArray.forEach(edge => {
            unduplicatedNodeArrayIds[edge.from] = 0;
            unduplicatedNodeArrayIds[edge.to] = 0;
        });
        const targetNodeIds: IdType[] = Object.keys(unduplicatedNodeArrayIds);
        const nodeArray: Node[] = this.networkData.nodes.get(targetNodeIds);
        this._ns.neighborsEmitter.next(
            new GraphDataCollection(nodeArray.map((node: Node) => <NodeDTO>node), edgeArray.map((edge: Edge) => <EdgeDTO>edge))
        );
    }

    getNodesNeighbours(idOrigins: IdType[]) {
        for (const i of idOrigins) {
            this._ns.getGraphData(i).subscribe(
                (data: GraphDataCollection) => {
                    this.addNodes(data.nodes, data.edges);
                },
                error => {
                    console.log('[NETWORK] Error lors de la récupération des voisins.');
                }
            );
        }
    }

    getState(): NetworkState {
        return this._ns.networkState.getValue();
    }

    addNodes(nodes: Node[], edges: Edge[]) {
        this.network.storePositions();
        addNodes(this.networkData, nodes, edges);
        this.clusterNodes();
    }

    removeNodes(idNodes: IdType[], idEdges: IdType[]) {
        this.network.storePositions();
        this.networkData.nodes.remove(idNodes);
        this.networkData.edges.remove(idEdges);
    }

    updateGraphNodeFiltering() {
        const updateNodeList: { id: IdType; hidden: boolean; physics: boolean }[] = [];
        this.networkData.nodes.forEach(node => {
            const hiddenStatus = this._ns.isHidden(node['objectType']).hidden;
            const physicsStatus = this._ns.isHidden(node['objectType']).physics;
            if (node.hidden !== hiddenStatus && node.physics !== physicsStatus) {
                updateNodeList.push({ id: node.id, hidden: hiddenStatus, physics: physicsStatus });
            }
        });
        this.networkData.nodes.update(updateNodeList);
        this.network.redraw();
        this.clusterNodes();
    }

    updateDataContent() {
        const updatedList: DataContentInfo[] = [];
        this.networkData.nodes.forEach(item => {
            const node: NodeDTO = <NodeDTO>item;
            updatedList.push(new DataContentInfo(node.label, node.id, node.mongoId, node.objectType, node.title, node.image));
        });
        this._ns.networkDataContent.next(updatedList);
    }

    addJSONFileElements(json: File) {
        const reader: FileReader = new FileReader();
        let jsonString: string;
        reader.onload = (event: Event) => {
            jsonString = (<FileReaderEventTarget>event.target).result;
            const graphyDatas: IGraphyNodeDTO[] = JSON.parse(jsonString);
            const data = new GraphDataCollection([], []);
            data.nodes = graphyDatas.map((item: IGraphyNodeDTO) =>
                NetworkService.getNodeDto(
                    item.label,
                    item.type,
                    item.id,
                    item.idMongo,
                    '',
                    item.symbole,
                    this._ns.isHidden(item.type).hidden,
                    this._ns.isHidden(item.type).physics
                )
            );
            data.edges = graphyDatas
                .map(item => NetworkService.getEdgeCollection(item.id, item.to))
                .reduce((acc, currentValue) => acc.concat(currentValue), []);
            this.addNodes(data.nodes, data.edges);
        };
        reader.readAsText(json);
    }

    clusterNodes() {
        if (!this._ns.networkState.getValue().CLUSTER_NODES || this.networkData.nodes.length < 50) {
            return;
        }
        const clusterOption = {
            joinCondition: (nodeOptions: any) => {
                if (nodeOptions && nodeOptions['objectType'] && !nodeOptions['hidden']) {
                    return nodeOptions['objectType'] === 'RawData' ? true : nodeOptions['amountOfConnections'] > 10;
                }
                return false;
            },
            processProperties: (clusterOptions: any, childNodesOptions: any[]) => {
                clusterOptions['label'] = `${childNodesOptions ? childNodesOptions.length : 1}`;
                return clusterOptions;
            },
            clusterNodeProperties: {
                borderWidth: 3,
                shape: 'circle',
                size: 50,
                color: {
                    background: 'rgba(255, 0, 0, 0.7)',
                    border: 'rgba(0, 102, 255, 0.7)'
                },
                shadow: {
                    enabled: true,
                    color: 'rgba(0, 102, 255, 0.9)',
                    size: 10,
                    x: 5,
                    y: 5
                },
                font: {
                    color: 'white',
                    bold: {
                        size: 18
                    }
                }
            }
        };
        this.network.cluster(clusterOption);
    }

    onImageDropped(params: DragParameter) {
        const bound: ClientRect = this._networkRef.nativeElement.getBoundingClientRect();
        const mouseX: number = params.event.clientX - bound.left;
        const mouseY: number = params.event.clientY - bound.top;
        const targetNodeId: IdType = this.network.getNodeAt({ x: mouseX, y: mouseY });
        if (targetNodeId) {
            const node: Node = this.networkData.nodes.get(targetNodeId);
            const targetNodeMongoId: string = (<NodeDTO>node).mongoId;
            if (!targetNodeMongoId) {
                return;
            }
            this._ns.updateData(targetNodeMongoId, params.data).subscribe(
                (data: GenericModel) => {
                    console.log('Update du symbole successful');
                    const dataSymbol = data[getGenericSymbolProperty(data)];
                    this.networkData.nodes.update({ id: targetNodeId, image: dataSymbol });
                    this.updateDataContent();
                },
                error => {
                    console.log("Error lors de l'update du symbole de l'entity, Id: " + targetNodeMongoId);
                }
            );
        }
    }

    onActionReceived(action: string) {
        const networkState: NetworkState = this._ns.networkState.getValue();
        switch (action) {
            case 'ADD_NODES':
                break;
            case 'DELETE_NODES':
                this.removeNodes(this.network.getSelection().nodes, this.network.getSelection().edges);
                break;
            case 'UPDATE_NODES':
                break;
            case 'ADD_NEIGHBOURS':
                networkState.ADD_NEIGHBOURS = !networkState.ADD_NEIGHBOURS;
                break;
            case 'CLUSTER_NODES':
                networkState.CLUSTER_NODES = !networkState.CLUSTER_NODES;
                if (networkState.CLUSTER_NODES) {
                    this.clusterNodes();
                }
                break;
            case 'CREATE_RELATIONS':
                networkState.CREATE_RELATIONS = !networkState.CREATE_RELATIONS;
                this.createMesh();
                break;
            case 'PHYSICS_ENABLED':
                networkState.PHYSICS_ENABLED = !networkState.PHYSICS_ENABLED;
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'SORT_METHOD_HU':
                networkState.SORT_METHOD = 'hubsize';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'SORT_METHOD_DIR':
                networkState.SORT_METHOD = 'directed';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'LAYOUT_FREE':
                networkState.LAYOUT_FREE = !networkState.LAYOUT_FREE;
                this.network.setOptions({ layout: this.getLayoutOption() });
                // Obliger de remettre l'option physic sinon elle est écrasée avec le changement de layout
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'LAYOUT_DIR_UD':
                networkState.LAYOUT_DIR = 'UD';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'LAYOUT_DIR_DU':
                networkState.LAYOUT_DIR = 'DU';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'LAYOUT_DIR_LR':
                networkState.LAYOUT_DIR = 'LR';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'LAYOUT_DIR_RL':
                networkState.LAYOUT_DIR = 'RL';
                this.network.setOptions({ layout: this.getLayoutOption() });
                this.network.setOptions({ physics: this.getPhysicsOption() });
                break;
            case 'AUTO_REFRESH':
                networkState.AUTO_REFRESH = !networkState.AUTO_REFRESH;
                break;
            case 'ADD_BIOGRAPHICS':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'Biographics');
                break;
            case 'ADD_EVENT':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'Event');
                break;
            case 'ADD_LOCATION':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'Location');
                break;
            case 'ADD_EQUIPMENT':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'Equipment');
                break;
            case 'ADD_ORGANISATION':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'Organisation');
                break;
            case 'ADD_RAWDATA':
                networkState.ENTITIES_FILTER = updateUniqueElementArray(networkState.ENTITIES_FILTER, 'RawData');
                break;
            default:
                break;
        }
        this._ns.networkState.next(networkState);
    }

    private createMesh() {
        console.log('creating mesh');
        console.log('for ' + this.network.getSelectedNodes().length + ' selected objects');
        this.network.getSelectedNodes().forEach((sourceId: IdType) => {
            this.network.getSelectedNodes().forEach((targetId: IdType) => {
                this.doCreateRelation(sourceId, targetId);
            });
        });
    }

    private doCreateRelation(sourceId: IdType, targetId: IdType) {
        this._ns
            .createRelation(sourceId, targetId)
            .subscribe(
                (res: HttpResponse<string>) => this.onSuccess(),
                (res: HttpErrorResponse) => this.onCreated(res, sourceId, targetId)
            );
    }

    private onSuccess() {
        console.log('ok');
    }

    private onCreated(res: HttpErrorResponse, sourceId: IdType, targetId: IdType) {
        if (res.status === 201) {
            // created
            const data = new GraphDataCollection([], []);
            data.nodes = [];
            data.edges = [NetworkService.getEdgeDto(sourceId, targetId)];
            this.addNodes(data.nodes, data.edges);
        } else {
            console.log('failed to create relation');
        }
    }
}
