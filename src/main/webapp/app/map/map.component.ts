import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
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
import DrawInteraction from 'ol/interaction/draw';
import SnapInteraction from 'ol/interaction/snap';
import ModifyInteraction from 'ol/interaction/modify';

import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Icon from 'ol/style/icon';
import Style from 'ol/style/style';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import { FigureStyle, MapState } from '../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { pairwise, startWith } from 'rxjs/internal/operators';
import getOwnPropertyDescriptor = Reflect.getOwnPropertyDescriptor;

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styles: [':host { flex-grow: 1 }']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
    rawDataSource: VectorSource = new VectorSource();
    dessinSource: VectorSource = new VectorSource();
    vectorLayer: VectorLayer;
    dessinLayer: VectorLayer;
    _map: Map;

    selectInteraction: SelectInteration;
    drawInteraction: DrawInteraction;
    snapInteraction: SnapInteraction;
    modifyInteraction: ModifyInteraction;

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

    featureSourceSubs: Subscription;
    featureSelectorSubs: Subscription;

    @HostListener('window:resize')
    onResize() {
        this.internalOnResize();
    }

    constructor(private er: ElementRef, private cdr: ChangeDetectorRef, private ms: MapService) {
        this.vectorLayer = new VectorLayer({
            source: this.rawDataSource,
            style: (feature: Feature) => this.styleFunction(feature, false)
        });
        this.dessinLayer = new VectorLayer({
            source: this.dessinSource,
            style: (feature: Feature) => this.getDessinStyle()
        });
        this.selectInteraction = new SelectInteration({
            style: (feature: Feature) => this.styleFunction(feature, true),
            multi: true
        });
        this.modifyInteraction = new ModifyInteraction({
            source: this.dessinSource
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
        this.featureSourceSubs = this.ms.featureSource.subscribe((features: Feature[]) => {
            this.rawDataSource.addFeatures(features);
        });
        this.featureSelectorSubs = this.ms.outsideFeatureSelector.subscribe((ids: string[]) => {
            if (ids && ids.length) {
                this.selectAndGoTo(ids[0]);
            }
        });
        this.initDessinTools();
    }

    ngOnDestroy() {
        if (this.featureSourceSubs) {
            this.featureSourceSubs.unsubscribe();
        }
        if (this.featureSelectorSubs) {
            this.featureSelectorSubs.unsubscribe();
        }
    }

    private initMap() {
        // const readFeatures = new GeoJSON().readFeatures(GEO_JSON_OBJECT);
        this._map = new Map({
            layers: [
                new TileLayer({
                    source: new OSM()
                }),
                this.vectorLayer,
                this.dessinLayer
            ],
            target: 'map',
            controls: control.defaults({
                attributionOptions: {
                    collapsible: false
                }
            }),
            view: new View({
                center: [0, 0],
                zoom: 2,
                minZoom: 1,
                maxZoom: 20
            })
        });
        setTimeout(() => {
            window.dispatchEvent(new Event('resize'));
        });
        this._map.addInteraction(this.selectInteraction);
        this._map.addInteraction(this.modifyInteraction);
    }

    private initDessinTools() {
        this.ms.dessinStates
            .pipe(
                startWith(null),
                pairwise()
            )
            .subscribe((values: FigureStyle[]) => {
                if (!this.getMapStates().DESSIN_ENABLED) {
                    return;
                }
                if (values[0] == null || values[0].form !== values[1].form) {
                    this.removeDrawInteraction();
                    this.addDrawInteraction();
                }
            });
    }

    addDrawInteraction() {
        this.drawInteraction = new DrawInteraction({
            source: this.dessinSource,
            type: this.getDessinStates().form === 'Rectangle' ? 'Circle' : this.getDessinStates().form,
            geometryFunction: this.getDessinStates().form === 'Rectangle' ? DrawInteraction.createBox() : null
        });
        this._map.addInteraction(this.drawInteraction);
        this.snapInteraction = new SnapInteraction({
            source: this.dessinSource
        });
        this._map.addInteraction(this.snapInteraction);
    }

    removeDrawInteraction() {
        this._map.removeInteraction(this.drawInteraction);
        this._map.removeInteraction(this.snapInteraction);
    }

    getDessinStates(): FigureStyle {
        return this.ms.dessinStates.getValue();
    }

    getDessinStyle(): Style {
        return new Style({
            stroke: new Stroke({
                color: this.getDessinStates().strokeColor,
                lineDash: [this.getDessinStates().type],
                width: this.getDessinStates().size
            }),
            fill: new Fill({
                color: this.getDessinStates().fillColor
            }),
            image: new Circle({
                radius: 10,
                fill: new Fill({
                    color: this.getDessinStates().fillColor
                }),
                stroke: new Stroke({
                    color: this.getDessinStates().strokeColor,
                    lineDash: [this.getDessinStates().type],
                    width: this.getDessinStates().size
                })
            })
        });
    }

    selectAndGoTo(objectId: string) {
        this.selectInteraction.getFeatures().clear();
        const selectedFeat: Feature = this.rawDataSource.getFeatureById(objectId);
        if (selectedFeat) {
            this.selectInteraction.getFeatures().push(selectedFeat);
            this._map.getView().fit(selectedFeat.getGeometry().getExtent(), { duration: 1500 });
        }
    }

    getMapStates(): MapState {
        return this.ms.mapStates.getValue();
    }

    styleFunction(feature: Feature, isSelected: boolean) {
        const mainStyle: Style = this.getStyle(feature.getGeometry().getType());
        if (isSelected) {
            mainStyle.setImage(this.selectedCircleImage);
        }
        if (feature.getGeometry().getType() === 'Point') {
            const iconStyle: Style = this.getIconStyle(feature);
            const zoom = this._map.getView().getZoom();
            if (zoom > 5 && this.getMapStates().DISPLAY_LABEL && feature.get('label')) {
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
        const src: string = MapService.getImageIconUrl(objectType);
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

    onActionReceived(action: string) {
        const mapState = this.getMapStates();
        switch (action) {
            case 'F_ALL_DATA':
                if (mapState.FILTER_TYPE !== 'all') {
                    mapState.FILTER_TYPE = 'all';
                    this.onFilterChanged();
                }
                break;
            case 'F_LOCATIONS_ONLY':
                if (mapState.FILTER_TYPE !== 'locations') {
                    mapState.FILTER_TYPE = 'locations';
                    this.onFilterChanged();
                }
                break;
            case 'F_IMAGES_ONLY':
                if (mapState.FILTER_TYPE !== 'images') {
                    mapState.FILTER_TYPE = 'images';
                    this.onFilterChanged();
                }
                break;
            case 'F_NO_FILTER':
                if (mapState.FILTER_TYPE) {
                    mapState.FILTER_TYPE = null;
                    this.onFilterChanged();
                }
                break;
            case 'DESSIN_ENABLED':
                mapState.DESSIN_ENABLED = !mapState.DESSIN_ENABLED;
                if (!mapState.DESSIN_ENABLED && this.drawInteraction) {
                    this.removeDrawInteraction();
                    this._map.addInteraction(this.selectInteraction);
                } else if (mapState.DESSIN_ENABLED) {
                    this._map.removeInteraction(this.selectInteraction);
                    this.addDrawInteraction();
                }
                break;
            default:
                break;
        }
        this.ms.mapStates.next(mapState);
    }

    onFilterChanged() {
        this.rawDataSource.clear();
    }
}
