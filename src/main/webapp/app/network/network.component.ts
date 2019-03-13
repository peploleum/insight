import { AfterContentInit, AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSet, Edge, IdType, Network, Node, Options } from 'vis';
import { NetworkService } from './network.service';
import { Subscription } from 'rxjs/index';
import { ActivatedRoute } from '@angular/router';
import { EdgeDTO, GraphDataCollection, GraphDataSet, IGraphyNodeDTO, NodeDTO } from 'app/shared/model/node.model';
import { RawData } from 'app/shared/model/raw-data.model';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { FileReaderEventTarget } from '../shared/util/insight-util';
import { SideMediatorService } from '../side/side-mediator.service';
import { DataContentInfo, NetworkState } from '../shared/util/network.util';
import { ToolbarState } from '../shared/util/side.util';

@Component({
    selector: 'ins-network',
    templateUrl: './network.component.html',
    styles: [':host { flex-grow: 1}']
})
export class NetworkComponent implements OnInit, AfterViewInit, AfterContentInit, OnDestroy {
    @ViewChild('network', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;

    graphDataSubscription: Subscription;
    actionClickedSubs: Subscription;
    networkStateSubs: Subscription;
    dataSelectedSubs: Subscription;

    constructor(private _ns: NetworkService, private activatedRoute: ActivatedRoute, private _sms: SideMediatorService) {}

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
        this.activatedRoute.data.subscribe(({ originNode }) => {
            if (originNode) {
                const theRawData: RawData = <RawData>originNode;
                if (theRawData.externalId != null) {
                    this._ns
                        .getNodeProperties(theRawData.externalId)
                        .pipe(
                            filter((response: HttpResponse<IGraphyNodeDTO>) => response.ok),
                            map((response: HttpResponse<IGraphyNodeDTO>) => {
                                const rawData: IGraphyNodeDTO = response.body;
                                return NetworkService.getNodeDto(rawData.label, rawData.type, rawData.id, rawData.mongoId);
                            })
                        )
                        .subscribe((nodeDTO: NodeDTO) => {
                            this.addNodes([nodeDTO], []);
                            this.getNodesNeighbours([nodeDTO.id]);
                        });
                } else {
                    this.getMockData();
                }
            } else {
                this.getMockData();
            }
        });
        this.networkStateSubs = this._ns.networkState.subscribe(state => {
            const updatedEventThreadToolbar = this._ns.getUpdatedEventThreadToolbar();
            this._sms.updateToolbarState(new ToolbarState('EVENT_THREAD', updatedEventThreadToolbar));
        });
        this.actionClickedSubs = this._sms._onNewActionClicked.subscribe(action => {
            this.onActionReceived(action);
        });
        this.dataSelectedSubs = this._sms._onNewDataSelected.subscribe((selectedIds: string[]) => {
            this.network.selectNodes(selectedIds, true);
            this._sms._selectedData.next(selectedIds);
        });
    }

    ngOnDestroy() {
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
                levelSeparation: 100,
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
        this.network.on('hoverNode', node => {
            console.log(node);
        });
        this.network.on('selectNode', properties => {
            if (this.getState().ADD_NEIGHBOURS) {
                this.getNodesNeighbours(properties.nodes);
            }
            if (!this.getState().CLUSTER_NODES) {
                const clusteredIds: IdType[] = (<IdType[]>properties.nodes).filter((id: IdType) => this.network.isCluster(id));
                clusteredIds.forEach(id => this.network.openCluster(id));
            }
            this._sms._selectedData.next(<string[]>properties.nodes);
            this.emitNeighborsOnSelection(properties.nodes, properties.edges);
        });
        this.network.on('deselectNode', properties => {
            this._sms._selectedData.next(<string[]>properties.nodes);
        });
        this.networkData.nodes.on('add', (event, properties) => {
            this.updateDataContent();
        });
        this.networkData.nodes.on('remove', (event, properties) => {
            this.updateDataContent();
        });
    }

    emitNeighborsOnSelection(nodeIds: IdType[], edgeIds: IdType[]) {
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
            this._ns.getGraphData(i).subscribe((data: GraphDataCollection) => {
                this.addNodes(data.nodes, data.edges);
            });
        }
    }

    getState(): NetworkState {
        return this._ns.networkState.getValue();
    }

    addNodes(nodes: Node[], edges: Edge[]) {
        this.network.storePositions();
        this.networkData.nodes.add(nodes);
        this.networkData.edges.add(edges);
    }

    removeNodes(idNodes: IdType[], idEdges: IdType[]) {
        this.network.storePositions();
        this.networkData.nodes.remove(idNodes);
        this.networkData.edges.remove(idEdges);
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
            data.nodes = graphyDatas.map((item: IGraphyNodeDTO) => NetworkService.getNodeDto(item.label, item.type, item.id));
            data.edges = graphyDatas
                .map(item => NetworkService.getEdgeCollection(item.id, item.to))
                .reduce((acc, currentValue) => acc.concat(currentValue), []);
            this.addNodes(data.nodes, data.edges);
        };
        reader.readAsText(json);
    }

    clusterNodes() {
        const clusterOption = {
            clusterNodeProperties: {
                label: 'Cluster',
                borderWidth: 3,
                shape: 'circle',
                size: 30,
                color: {
                    background: 'red'
                },
                font: {
                    bold: {
                        size: 18
                    }
                }
            }
        };
        this.network.clusterByHubsize(3, clusterOption);
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
            default:
                break;
        }
        this._ns.networkState.next(networkState);
    }
}
