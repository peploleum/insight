import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { MapService } from './map.service';
import Map from 'ol/map';
import View from 'ol/view';
import VectorSource from 'ol/source/vector';
import VectorLayer from 'ol/layer/vector';
import TileLayer from 'ol/layer/tile';
import OSM from 'ol/source/osm';
import control from 'ol/control';
import Feature from 'ol/feature';
import SelectInteration from 'ol/interaction/select';

import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Icon from 'ol/style/icon';
import Style from 'ol/style/style';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import { MapState } from '../shared/util/map-utils';
import { IMAGE_URL_BIO, IMAGE_URL_DEFAULT, IMAGE_URL_EQUIP, IMAGE_URL_RAW } from '../network/network.service';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styles: [':host { flex-grow: 1 }']
})
export class MapComponent implements OnInit, AfterViewInit {
    rawDataSource: VectorSource = new VectorSource();
    vectorLayer: VectorLayer;
    _map: Map;
    selectInteraction: SelectInteration;

    mapStates: MapState = new MapState(true, true);

    private circleImage = new Circle({
        radius: 13,
        fill: new Fill({
            color: 'rgba(203, 65, 42, 0.1)'
        }),
        stroke: new Stroke({ color: '#ffc600', width: 3 })
    });
    private selectedCircleImage = new Circle({
        radius: 13,
        fill: new Fill({
            color: 'rgba(203, 65, 42, 0.1)'
        }),
        stroke: new Stroke({ color: '#cb412a', width: 3 })
    });

    computedHeight = 0;

    @HostListener('window:resize')
    onResize() {
        this.internalOnResize();
    }

    constructor(private er: ElementRef, private cdr: ChangeDetectorRef, private ms: MapService) {
        this.vectorLayer = new VectorLayer({
            source: this.rawDataSource,
            style: (feature: Feature) => this.styleFunction(feature, false)
        });
        this.selectInteraction = new SelectInteration({
            style: (feature: Feature) => this.styleFunction(feature, true),
            multi: true
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
        this.getFeatureForId([]);
    }

    getFeatureForId(ids: string[]) {
        this.ms.getFeaturesForIds([]).subscribe((features: Feature[]) => {
            this.rawDataSource.addFeatures(features);
        });
    }

    private initMap() {
        // const readFeatures = new GeoJSON().readFeatures(GEO_JSON_OBJECT);
        this._map = new Map({
            layers: [
                new TileLayer({
                    source: new OSM()
                }),
                this.vectorLayer
            ],
            target: 'map',
            controls: control.defaults({
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
            window.dispatchEvent(new Event('resize'));
        });
        this._map.addInteraction(this.selectInteraction);
    }

    styleFunction(feature: Feature, isSelected: boolean) {
        const mainStyle: Style = this.getStyle(feature.getGeometry().getType());
        if (isSelected) {
            mainStyle.setImage(this.selectedCircleImage);
        }
        if (feature.getGeometry().getType() === 'Point') {
            const iconStyle: Style = this.getIconStyle(feature);
            const zoom = this._map.getView().getZoom();
            if (zoom > 5 && this.mapStates.DISPLAY_LABEL && feature.get('label')) {
                mainStyle.getText().setText(feature.get('label'));
            } else {
                mainStyle.getText().setText('');
            }
            return [iconStyle, mainStyle];
        }
        return [mainStyle];
    }

    getIconStyle(feature: Feature): Style {
        const objectType = feature.get('objectType');
        let src: string;
        switch (objectType) {
            case 'RawData':
                src = IMAGE_URL_RAW;
                break;
            case 'Equipement':
                src = IMAGE_URL_EQUIP;
                break;
            case 'Location':
                src = IMAGE_URL_RAW;
                break;
            case 'Biographics':
                src = IMAGE_URL_BIO;
                break;
            case 'Organisation':
                src = IMAGE_URL_DEFAULT;
                break;
            case 'Event':
                src = IMAGE_URL_DEFAULT;
                break;
            default:
                src = IMAGE_URL_DEFAULT;
                break;
        }
        return new Style({
            image: new Icon({
                anchor: [0.5, 0.5],
                scale: 0.05,
                src: `${src}`
            })
        });
    }

    getStyle(geometryType: string): Style {
        switch (geometryType) {
            case 'Point':
                return new Style({
                    image: this.circleImage,
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
            case 'LineString':
                return new Style({
                    stroke: new Stroke({
                        color: 'green',
                        width: 1
                    })
                });
                break;
            case 'MultiLineString':
                return new Style({
                    stroke: new Stroke({
                        color: 'green',
                        width: 1
                    })
                });
                break;
            case 'MultiPoint':
                return new Style({
                    image: this.circleImage
                });
                break;
            case 'MultiPolygon':
                return new Style({
                    stroke: new Stroke({
                        color: 'yellow',
                        width: 1
                    }),
                    fill: new Fill({
                        color: 'rgba(255, 255, 0, 0.1)'
                    })
                });
                break;
            case 'Polygon':
                return new Style({
                    stroke: new Stroke({
                        color: 'blue',
                        lineDash: [4],
                        width: 3
                    }),
                    fill: new Fill({
                        color: 'rgba(0, 0, 255, 0.1)'
                    })
                });
                break;
            case 'GeometryCollection':
                return new Style({
                    stroke: new Stroke({
                        color: 'magenta',
                        width: 2
                    }),
                    fill: new Fill({
                        color: 'magenta'
                    }),
                    image: new Circle({
                        radius: 10,
                        fill: null,
                        stroke: new Stroke({
                            color: 'magenta'
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
