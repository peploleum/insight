import { AfterContentInit, AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DataSet, Edge, IdType, Network, Node, Options } from 'vis';
import { NetworkService } from './network.service';
import { Subscription } from 'rxjs/index';
import { ActivatedRoute } from '@angular/router';
import { GraphDataCollection, GraphDataSet, NodeDTO } from 'app/shared/model/node.model';

@Component({
    selector: 'ins-network',
    templateUrl: './network.component.html',
    styles: [':host { flex-grow: 1}']
})
export class NetworkComponent implements OnInit, AfterViewInit, AfterContentInit {
    @ViewChild('network', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;
    networkActionStates = {};

    graphDataSubscription: Subscription;

    constructor(private _ns: NetworkService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {}

    ngAfterViewInit() {
        this.initNetwork();
        this.initNetworkEventListener();
        this.activatedRoute.data.subscribe(({ originNode }) => {
            if (originNode) {
                const theNode: NodeDTO = <NodeDTO>originNode;
                this.addNodes([theNode], []);
                this.getNodesNeighbours([theNode.id]);
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

    initNetwork() {
        this.networkData = new GraphDataSet(new DataSet(), new DataSet());
        if (this._networkRef && !this.network) {
            const options: Options = {
                layout: {
                    hierarchical: {
                        enabled: true,
                        levelSeparation: 100
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
            this.network = new Network(this._networkRef.nativeElement, this.networkData, options);
        }
    }

    initNetworkEventListener() {
        this.network.on('hoverNode', node => {
            console.log(node);
        });
        this.network.on('selectNode', properties => {
            if (this.networkActionStates['ADD_NEIGHBOURS']) {
                this.getNodesNeighbours(properties.nodes);
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
                this.networkActionStates['ADD_NEIGHBOURS'] = !this.networkActionStates['ADD_NEIGHBOURS'];
                break;
            case 'CLUSTER_NODES':
                this.networkActionStates['CLUSTER_NODES'] = !this.networkActionStates['CLUSTER_NODES'];
                if (this.networkActionStates['CLUSTER_NODES']) {
                    this.clusterNodes();
                } else {
                    const clusteredIds: IdType[] = this.networkData.nodes
                        .get()
                        .filter(node => this.network.isCluster(node.id))
                        .map(node => node.id);
                    clusteredIds.forEach(id => this.network.openCluster(id));
                }
                break;
            default:
                break;
        }
    }
}
