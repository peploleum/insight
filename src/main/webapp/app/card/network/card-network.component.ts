/**
 * Created by GFOLGOAS on 09/04/2019.
 */
import {
    AfterViewInit,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    OnDestroy,
    OnInit,
    Output,
    SimpleChanges,
    ViewChild
} from '@angular/core';
import { GraphDataCollection, GraphDataSet, IGraphyNodeDTO, NodeDTO } from '../../shared/model/node.model';
import { DataSet, Edge, IdType, Network, Node, Options } from 'vis';
import { NetworkService } from '../../network/network.service';
import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/internal/operators';

@Component({
    selector: 'ins-card-network',
    templateUrl: './card-network.component.html'
})
export class CardNetworkComponent implements OnInit, OnDestroy, AfterViewInit, OnChanges {
    @Input() externalId: string;
    @ViewChild('neighbor', { read: ElementRef }) networkRef: ElementRef;
    network: Network;
    networkData: GraphDataSet;

    @Output()
    nodeMongoIds: EventEmitter<string[]> = new EventEmitter();

    constructor(private _ns: NetworkService) {}

    ngOnInit() {}

    ngOnChanges(changes: SimpleChanges) {
        this.getNetworkData();
    }

    ngAfterViewInit() {
        this.initNetwork();
    }

    ngOnDestroy() {}

    getNetworkData() {
        if (this.externalId) {
            this._ns
                .getNodeProperties(this.externalId)
                .pipe(
                    filter((response: HttpResponse<IGraphyNodeDTO>) => response.ok),
                    map((response: HttpResponse<IGraphyNodeDTO>) => {
                        const data: IGraphyNodeDTO = response.body;
                        return NetworkService.getNodeDto(data.label, data.type, data.id, data.mongoId, '', data.image);
                    })
                )
                .subscribe((nodeDTO: NodeDTO) => {
                    this.clearDatasets();
                    this.addNodes([nodeDTO], []);
                    this.getNodesNeighbours(nodeDTO.id);
                });
        } else {
            this.clearDatasets();
        }
    }

    initNetwork() {
        this.networkData = new GraphDataSet(new DataSet(), new DataSet());
        if (this.networkRef && !this.network) {
            this.network = new Network(this.networkRef.nativeElement, this.networkData, this.getInitialNetworkOption());
        }
    }

    getInitialNetworkOption(): Options {
        return {
            autoResize: true,
            height: '100%',
            width: '100%',
            layout: this.getLayoutOption(),
            physics: {
                enabled: true
            },
            interaction: {
                selectable: false,
                hideEdgesOnDrag: true,
                hover: true,
                hoverConnectedEdges: true,
                keyboard: {
                    enabled: true
                },
                multiselect: false,
                navigationButtons: false
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
        this.networkData.nodes.add(nodes);
        this.networkData.edges.add(edges);
        this.network.fit();
    }

    getNodesNeighbours(idOrigin: IdType) {
        this._ns.getGraphData(idOrigin).subscribe(
            (data: GraphDataCollection) => {
                this.addNodes(data.nodes, data.edges);
                const mongoIds: string[] = this.networkData.nodes.map(node => (<NodeDTO>node).mongoId);
                this.nodeMongoIds.emit(mongoIds);
            },
            error => {
                console.log('[NETWORK] Error lors de la récupération des voisins.');
            }
        );
    }

    clearDatasets() {
        if (this.networkData) {
            this.networkData.nodes.clear();
            this.networkData.edges.clear();
        }
    }
}
