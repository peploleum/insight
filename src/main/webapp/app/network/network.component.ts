import { AfterContentInit, AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DataSet, Edge, IdType, Network, Node, Options } from 'vis';
import { NetworkService } from './network.service';
import { Subscription } from 'rxjs/index';
import { ActivatedRoute } from '@angular/router';
import { GraphDataCollection, GraphDataSet, IGraphyNodeDTO, NodeDTO } from 'app/shared/model/node.model';
import { RawData } from 'app/shared/model/raw-data.model';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { FileReaderEvent, FileReaderEventTarget } from '../shared/util/insight-util';

@Component({
    selector: 'ins-network',
    templateUrl: './network.component.html',
    styles: [':host { flex-grow: 1}']
})
export class NetworkComponent implements OnInit, AfterViewInit, AfterContentInit {
    @ViewChild('network', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;
    networkStates = { LAYOUT_DIR: 'UD', LAYOUT_FREE: false };

    graphDataSubscription: Subscription;

    constructor(private _ns: NetworkService, private activatedRoute: ActivatedRoute) {}

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
                console.log('ROUTING DONE');
                const theRawData: RawData = <RawData>originNode;
                console.log(theRawData);
                if (theRawData.externalId != null) {
                    console.log('ROUTING DONE ' + theRawData.externalId);
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
                            console.log('ROUTING DONE ' + nodeDTO);
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
    }

    getMockData() {
        this.graphDataSubscription = this._ns.getMockGraphData().subscribe((data: GraphDataCollection) => {
            this.addNodes(data.nodes, data.edges);
        });
    }

    ngAfterContentInit() {}

    getNetworkOption(): Options {
        return {
            layout: {
                hierarchical: {
                    enabled: !this.networkStates['LAYOUT_FREE'],
                    levelSeparation: 100,
                    direction: this.networkStates['LAYOUT_DIR']
                }
            },
            physics: {
                enabled: false
            },
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

    initNetwork() {
        this.networkData = new GraphDataSet(new DataSet(), new DataSet());
        if (this._networkRef && !this.network) {
            this.network = new Network(this._networkRef.nativeElement, this.networkData, this.getNetworkOption());
        }
    }

    initNetworkEventListener() {
        this.network.on('hoverNode', node => {
            console.log(node);
        });
        this.network.on('selectNode', properties => {
            if (this.networkStates['ADD_NEIGHBOURS']) {
                this.getNodesNeighbours(properties.nodes);
            }
            if (!this.networkStates['CLUSTER_NODES']) {
                const clusteredIds: IdType[] = (<IdType[]>properties.nodes).filter((id: IdType) => this.network.isCluster(id));
                clusteredIds.forEach(id => this.network.openCluster(id));
            }
        });
        this.networkData.nodes.on('add', (event, properties) => {
            console.log(properties);
        });
    }

    getNodesNeighbours(idOrigins: IdType[]) {
        for (const i of idOrigins) {
            this._ns.getGraphData(i).subscribe((data: GraphDataCollection) => {
                this.addNodes(data.nodes, data.edges);
            });
        }
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

    addJSONFileElements(json: File) {
        const reader: FileReader = new FileReader();
        let jsonString: string;
        reader.onload = (event: Event) => {
            jsonString = (<FileReaderEventTarget>event.target).result;
            const graphyDatas: IGraphyNodeDTO[] = JSON.parse(jsonString);
            const data = new GraphDataCollection([], []);
            data.nodes = graphyDatas.map((item: IGraphyNodeDTO) => NetworkService.getNodeDto(item.label, item.type, item.id));
            data.edges = NetworkService.getEdgeCollection(data.nodes[0].id, data.nodes);
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
        switch (action) {
            case 'ADD_NODES':
                break;
            case 'DELETE_NODES':
                this.removeNodes(this.network.getSelection().nodes, this.network.getSelection().edges);
                break;
            case 'UPDATE_NODES':
                break;
            case 'ADD_NEIGHBOURS':
                this.networkStates['ADD_NEIGHBOURS'] = !this.networkStates['ADD_NEIGHBOURS'];
                break;
            case 'CLUSTER_NODES':
                this.networkStates['CLUSTER_NODES'] = !this.networkStates['CLUSTER_NODES'];
                if (this.networkStates['CLUSTER_NODES']) {
                    this.clusterNodes();
                }
                break;
            case 'LAYOUT_FREE':
                this.networkStates['LAYOUT_FREE'] = !this.networkStates['LAYOUT_FREE'];
                this.network.setOptions(this.getNetworkOption());
                break;
            case 'LAYOUT_DIR_UD':
                this.networkStates['LAYOUT_DIR'] = 'UD';
                this.network.setOptions(this.getNetworkOption());
                break;
            case 'LAYOUT_DIR_DU':
                this.networkStates['LAYOUT_DIR'] = 'DU';
                this.network.setOptions(this.getNetworkOption());
                break;
            case 'LAYOUT_DIR_LR':
                this.networkStates['LAYOUT_DIR'] = 'LR';
                this.network.setOptions(this.getNetworkOption());
                break;
            case 'LAYOUT_DIR_RL':
                this.networkStates['LAYOUT_DIR'] = 'RL';
                this.network.setOptions(this.getNetworkOption());
                break;
            default:
                break;
        }
    }
}
