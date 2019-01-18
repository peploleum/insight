import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { Circle as CircleStyle, Fill, Stroke, Style } from 'ol/style';
import { Tile as TileLayer, Vector as VectorLayer } from 'ol/layer';
import { Feature } from 'ol/Feature';
import { OSM, Vector as VectorSource } from 'ol/source';
import { GeoJSON } from 'ol/format';
import { defaults as defaultControls } from 'ol/control.js';
import Map from 'ol/Map';
import View from 'ol/View';
import { GEO_JSON_OBJECT, MapService } from './map.service';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styles: [':host { flex-grow: 1 }']
})
export class MapComponent implements OnInit, AfterViewInit {
    rawDataSource: VectorSource = new VectorSource();
    vectorLayer: VectorLayer = new VectorLayer();

    private circleImage = new CircleStyle({
        radius: 5,
        fill: null,
        stroke: new Stroke({ color: 'red', width: 1 })
    });
    private styles = {
        Point: new Style({
            image: this.circleImage
        }),
        LineString: new Style({
            stroke: new Stroke({
                color: 'green',
                width: 1
            })
        }),
        MultiLineString: new Style({
            stroke: new Stroke({
                color: 'green',
                width: 1
            })
        }),
        MultiPoint: new Style({
            image: this.circleImage
        }),
        MultiPolygon: new Style({
            stroke: new Stroke({
                color: 'yellow',
                width: 1
            }),
            fill: new Fill({
                color: 'rgba(255, 255, 0, 0.1)'
            })
        }),
        Polygon: new Style({
            stroke: new Stroke({
                color: 'blue',
                lineDash: [4],
                width: 3
            }),
            fill: new Fill({
                color: 'rgba(0, 0, 255, 0.1)'
            })
        }),
        GeometryCollection: new Style({
            stroke: new Stroke({
                color: 'magenta',
                width: 2
            }),
            fill: new Fill({
                color: 'magenta'
            }),
            image: new CircleStyle({
                radius: 10,
                fill: null,
                stroke: new Stroke({
                    color: 'magenta'
                })
            })
        }),
        Circle: new Style({
            stroke: new Stroke({
                color: 'red',
                width: 2
            }),
            fill: new Fill({
                color: 'rgba(255,0,0,0.2)'
            })
        })
    };

    computedHeight = 0;

    @HostListener('window:resize')
    onResize() {
        this.internalOnResize();
    }

    constructor(private er: ElementRef, private cdr: ChangeDetectorRef, private ms: MapService) {
        this.vectorLayer = new VectorLayer({
            source: this.rawDataSource,
            style: feature => this.styleFunction(feature)
        });
    }

    internalOnResize() {
        console.log('RESIZE');
        console.log(this.er.nativeElement.offsetHeight);
        this.computedHeight = this.er.nativeElement.offsetHeight;
        this.cdr.detectChanges();
    }

    ngOnInit() {}

    ngAfterViewInit(): void {
        this.initMap();
        this.ms.getFeaturesForIds([]).subscribe((features: Feature[]) => {
            this.rawDataSource.addFeatures(features);
        });
    }

    private initMap() {
        const readFeatures = new GeoJSON().readFeatures(GEO_JSON_OBJECT);
        this.rawDataSource.addFeatures(readFeatures);
        const map = new Map({
            layers: [
                new TileLayer({
                    source: new OSM()
                }),
                this.vectorLayer
            ],
            target: 'map',
            controls: defaultControls({
                attributionOptions: {
                    collapsible: false
                }
            }),
            view: new View({
                center: [0, 0],
                zoom: 2
            })
        });
        setTimeout(() => {
            console.log('dispatch');
            window.dispatchEvent(new Event('resize'));
        });
    }

    styleFunction(feature: Feature) {
        return this.styles[feature.getGeometry().getType()];
    }
}
