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
            this.getNodesNeighbours(properties.nodes);
        });
        this.networkData.nodes.on('add', (event, properties) => {
            console.log(properties);
        });
    }

    getNodesNeighbours(idOrigins: string[]) {
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

    removeNodes(ids: IdType[]) {
        this.networkData.nodes.remove(ids);
        const removableEdges: Edge[] = this.networkData.edges.get().filter((edge: Edge) => {
            return ids.indexOf(edge.from) === -1 || ids.indexOf(edge.to) === -1;
        });
        this.networkData.edges.remove(removableEdges);
    }

    onActionReceived(action: string) {
        switch (action) {
            case 'ADD_NODES':
                const newNodes2 = [
                    { label: 'node 11', type: 'PERSONNE' },
                    { label: 'node 12', type: 'EVENT' },
                    { label: 'node 13', type: 'LOCATION' }
                ];
                const transformDto: Node[] = newNodes2.map(item => NetworkService.getNodeDto(item.label, item.type));
                this.addNodes(transformDto, []);
                break;
            case 'DELETE_NODES':
                break;
            case 'UPDATE_NODES':
                break;
            default:
                break;
        }
    }
}
