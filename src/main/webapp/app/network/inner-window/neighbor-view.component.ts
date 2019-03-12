/**
 * Created by gFolgoas on 12/03/2019.
 */
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/index';
import { GraphDataCollection, GraphDataSet } from '../../shared/model/node.model';
import { DataSet, Edge, Network, Node, Options } from 'vis';
import { NetworkService } from '../network.service';

@Component({
    selector: 'ins-neighbor-view',
    templateUrl: './neighbor-view.component.html'
})
export class NeighborViewComponent implements OnInit, OnDestroy, AfterViewInit {
    @ViewChild('neighbor', { read: ElementRef }) _networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;

    networkStates;
    stateSubs: Subscription;
    neighborsEmitterSubs: Subscription;

    constructor(private _ns: NetworkService) {}

    ngOnInit() {
        this.stateSubs = this._ns.networkState.subscribe(state => (this.networkStates = state));
    }

    ngAfterViewInit() {
        this.initNetwork();
        this.neighborsEmitterSubs = this._ns.neighborsEmitter.subscribe((data: GraphDataCollection) => {
            this.clearDatasets();
            this.addNodes(data.nodes, data.edges);
        });
    }

    ngOnDestroy() {
        if (this.stateSubs) {
            this.stateSubs.unsubscribe();
        }
        if (this.neighborsEmitterSubs) {
            this.neighborsEmitterSubs.unsubscribe();
        }
    }

    initNetwork() {
        this.networkData = new GraphDataSet(new DataSet(), new DataSet());
        if (this._networkRef && !this.network) {
            this.network = new Network(this._networkRef.nativeElement, this.networkData, this.getInitialNetworkOption());
        }
    }

    getInitialNetworkOption(): Options {
        return {
            height: '100%',
            width: '100%',
            layout: this.getLayoutOption(),
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

    getLayoutOption(): any {
        return {
            hierarchical: {
                enabled: true,
                levelSeparation: 100,
                direction: 'UD'
            }
        };
    }

    addNodes(nodes: Node[], edges: Edge[]) {
        this.network.storePositions();
        this.networkData.nodes.add(nodes);
        this.networkData.edges.add(edges);
    }

    clearDatasets() {
        this.networkData.nodes.clear();
        this.networkData.edges.clear();
    }
}
