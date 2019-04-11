/**
 * Created by GFOLGOAS on 09/04/2019.
 */
import { AfterViewInit, Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import Map from 'ol/map';
import View from 'ol/view';
import VectorSource from 'ol/source/vector';
import VectorLayer from 'ol/layer/vector';
import TileLayer from 'ol/layer/tile';
import OSM from 'ol/source/osm';
import control from 'ol/control';
import Feature from 'ol/feature';
import SelectInteration from 'ol/interaction/select';
import Cluster from 'ol/source/cluster';
import { GenericModel } from '../../shared/model/generic.model';
import { MapService } from '../../map/map.service';
import { expandClusterStyleFunction, insStyleFunction, setClusterRadius } from '../../shared/util/map-utils';

@Component({
    selector: 'ins-card-map',
    templateUrl: './card-map.component.html'
})
export class CardMapComponent implements OnInit, OnDestroy, AfterViewInit, OnChanges {
    @Input()
    source: GenericModel[];

    _map: Map;
    featureSource: VectorSource = new VectorSource({ wrapX: false });
    clusterSource: Cluster;
    featureLayer: VectorLayer;

    currentResolution: number;
    maxClusterCount: number;

    selectInteraction: SelectInteration;

    constructor() {
        this.clusterSource = new Cluster({
            distance: 40,
            source: this.featureSource,
            wrapX: false
        });
        this.featureLayer = new VectorLayer({
            source: this.clusterSource,
            style: (feature: Feature, resolution: number) => {
                if (resolution !== this.currentResolution) {
                    this.maxClusterCount = setClusterRadius(this.clusterSource.getFeatures(), resolution);
                    this.currentResolution = resolution;
                }
                return insStyleFunction(feature, resolution, this.maxClusterCount || 1);
            },
            zIndex: 1
        });
        this.selectInteraction = new SelectInteration({
            condition: evt => {
                return evt.type === 'pointermove' || evt.type === 'singleclick';
            },
            style: (feature: Feature, resolution: number) => expandClusterStyleFunction(feature, resolution)
        });
    }

    ngOnInit() {}

    ngOnChanges(changes: SimpleChanges) {
        this.onNewData(this.source);
    }

    ngAfterViewInit() {
        this.initMap();
    }

    ngOnDestroy() {}

    onNewData(source: GenericModel[]) {
        if (source) {
            this.clearFeatureSource();
            const features: Feature[] = source
                .map(data => MapService.getMapDataFromGeneric(data))
                .map(dto => MapService.getGeoJsonFromDto(dto))
                .filter(geo => geo !== null);
            this.featureSource.addFeatures(features);
            this._map.getView().fit(this.featureSource.getExtent());
        }
    }

    private initMap() {
        this._map = new Map({
            layers: [this.featureLayer],
            target: 'map',
            controls: control.defaults({
                attributionOptions: {
                    collapsible: false
                }
            }),
            view: new View({
                center: [0, 0],
                zoom: 2,
                minZoom: 2,
                maxZoom: 20,
                extent: [-15723249.59231732, -5106184.58202049, 16485479.638377108, 11546080.652074866]
            })
        });
        this._map.addLayer(
            new TileLayer({
                source: new OSM({ wrapX: false })
            })
        );
        this._map.addInteraction(this.selectInteraction);
    }

    clearFeatureSource() {
        this.featureSource.clear();
    }
}
