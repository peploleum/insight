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

import Circle from 'ol/style/circle';
import Stroke from 'ol/style/stroke';
import Style from 'ol/style/style';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import { GenericModel } from '../../shared/model/generic.model';
import { MapService } from '../../map/map.service';

@Component({
    selector: 'ins-card-map',
    templateUrl: './card-map.component.html'
})
export class CardMapComponent implements OnInit, OnDestroy, AfterViewInit, OnChanges {
    @Input()
    source: GenericModel[];

    _map: Map;
    featureSource: VectorSource = new VectorSource({ wrapX: false });
    featureLayer: VectorLayer;

    constructor() {
        this.featureLayer = new VectorLayer({
            source: this.featureSource,
            style: (feature: Feature) => this.getStyle(feature.getGeometry().getType()),
            zIndex: 1
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
    }

    clearFeatureSource() {
        this.featureSource.clear();
    }

    getStyle(geometryType: string): Style {
        switch (geometryType) {
            case 'Point':
                return new Style({
                    image: new Circle({
                        radius: 14,
                        fill: new Fill({
                            color: 'rgba(230, 240, 255, 1)'
                        }),
                        stroke: new Stroke({ color: '#ffc600', width: 3 })
                    }),
                    text: new Text({
                        font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
                        placement: 'point',
                        textBaseline: 'top',
                        offsetY: 10,
                        fill: new Fill({
                            color: 'black'
                        })
                    })
                });
                break;
            default:
                return new Style({
                    stroke: new Stroke({
                        color: 'red',
                        width: 2
                    }),
                    fill: new Fill({
                        color: 'rgba(255,0,0,0.2)'
                    })
                });
                break;
        }
    }
}
