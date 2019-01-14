import { AfterContentInit, AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DataSet, Edge, Network, Node, Options } from 'vis';
import { NetworkService } from './network.service';

@Component({
    selector: 'ins-network',
    templateUrl: './network.component.html',
    styles: [':host { flex-grow: 1}']
})
export class NetworkComponent implements OnInit, AfterViewInit, AfterContentInit {
    @ViewChild('network', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: any = { 'network-items': {} };

    constructor(private _ns: NetworkService) {}

    ngOnInit() {}

    ngAfterViewInit() {
        this.initNetwork();
        this._ns.getGraphData().subscribe(data => {
            this.addNodes(data['nodes'], data['edges']);
        });
    }

    ngAfterContentInit() {}

    initNetwork() {
        this.networkData['network-items']['nodes'] = new DataSet();
        this.networkData['network-items']['edges'] = new DataSet();
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
                    multiselect: true
                },
                nodes: {
                    shape: 'circularImage'
                }
            };
            this.network = new Network(this._networkRef.nativeElement, this.networkData['network-items'], options);
        }
    }

    addNodes(nodes: Node[], edges: Edge[]) {
        this.network.storePositions();
        this.networkData['network-items']['nodes'].add(nodes);
        this.networkData['network-items']['edges'].add(edges);
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
