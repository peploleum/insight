import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { MapService } from './map.service';
import Map from 'ol/map';
import View from 'ol/view';
import VectorSource from 'ol/source/vector';
import VectorLayer from 'ol/layer/vector';
import TileLayer from 'ol/layer/tile';
import OSM from 'ol/source/osm';
import BingMaps from 'ol/source/bingmaps';
import control from 'ol/control';
import Feature from 'ol/feature';
import SelectInteration from 'ol/interaction/select';
import DrawInteraction from 'ol/interaction/draw';
import SnapInteraction from 'ol/interaction/snap';
import ModifyInteraction from 'ol/interaction/modify';
import DragAndDropInteraction from 'ol/interaction/draganddrop';
import KML from 'ol/format/kml';
import TileWMS from 'ol/source/tilewms';

import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Icon from 'ol/style/icon';
import Style from 'ol/style/style';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import { FigureStyle, MapLayer, MapState, ZoomToFeatureRequest } from '../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { pairwise, startWith } from 'rxjs/internal/operators';
import { ToolbarButtonParameters, UUID } from '../shared/util/insight-util';
import { SideMediatorService } from '../side/side-mediator.service';
import { EventThreadParameters, SideAction, SideParameters, ToolbarState } from '../shared/util/side.util';
import { IRawData } from '../shared/model/raw-data.model';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styles: [':host { flex-grow: 1 }']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
    rawDataSource: VectorSource = new VectorSource({ wrapX: false });
    geoMarkerSource: VectorSource = new VectorSource({ wrapX: false });
    featureLayer: VectorLayer;
    geoMarkerLayer: VectorLayer;
    _map: Map;

    selectInteraction: SelectInteration;
    drawInteraction: DrawInteraction;
    snapInteraction: SnapInteraction;
    modifyInteraction: ModifyInteraction;
    dragAndDropInteraction: DragAndDropInteraction;

    private circleImage = new Circle({
        radius: 14,
        fill: new Fill({
            color: 'rgba(230, 240, 255, 1)'
        }),
        stroke: new Stroke({ color: '#ffc600', width: 3 })
    });
    private selectedCircleImage = new Circle({
        radius: 14,
        fill: new Fill({
            color: 'rgba(230, 240, 255, 1)'
        }),
        stroke: new Stroke({ color: '#cb412a', width: 4 })
    });

    computedHeight = 0;

    featureSourceSubs: Subscription;
    geoMarkerSourceSubs: Subscription;
    outsideFeatureSelectorSubs: Subscription;
    layerSubs: Subscription;
    zoomToLayerSubs: Subscription;
    zoomToFeatureSubs: Subscription;
    actionEmitterSubs: Subscription;
    pinnedGeoMarkerSubs: Subscription;
    mapStatesSubs: Subscription;
    newDataReceivedSubs: Subscription;
    newEventThreadSearchValueSubs: Subscription;

    @HostListener('window:resize')
    onResize() {
        this.internalOnResize();
    }

    constructor(private er: ElementRef, private cdr: ChangeDetectorRef, private ms: MapService, private sms: SideMediatorService) {
        this.featureLayer = new VectorLayer({
            source: this.rawDataSource,
            style: (feature: Feature) => this.mainStyleFunction(feature, false),
            zIndex: 1
        });
        this.geoMarkerLayer = new VectorLayer({
            source: this.geoMarkerSource,
            style: (feature: Feature) => this.geoMarkerStyleFunction(feature, false),
            zIndex: 1
        });

        this.selectInteraction = new SelectInteration({
            style: (feature: Feature) => this.mainStyleFunction(feature, true),
            multi: true
        });
        this.dragAndDropInteraction = new DragAndDropInteraction({
            formatConstructors: [KML],
            projection: 'EPSG:3857'
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
        this.initMapLayerListener();
        this.mapStatesSubs = this.ms.mapStates
            .pipe(
                startWith(null),
                pairwise()
            )
            .subscribe((states: MapState[]) => {
                const needReload: boolean = states[0] !== null && typeof states[0] !== 'undefined';
                const newState = states[1];

                const newSideParameters: SideParameters<EventThreadParameters> = new SideParameters<EventThreadParameters>(
                    'EVENT_THREAD',
                    new EventThreadParameters(newState.AUTO_REFRESH, newState.FILTER_TYPE)
                );
                this.sms.updateSideParameters(newSideParameters);

                const updatedEventThreadToolbar: ToolbarButtonParameters[] = this.ms.getUpdatedEventThreadToolbar();
                this.sms.updateToolbarState(new ToolbarState('EVENT_THREAD', updatedEventThreadToolbar));

                const sideActions: SideAction[] = [];
                if (newState.AUTO_REFRESH) {
                    sideActions.push(new SideAction('EVENT_THREAD', 'TRIGGER_AUTO_REFRESH'));
                }
                if (needReload) {
                    sideActions.push(new SideAction('EVENT_THREAD', 'CLEAR'));
                    sideActions.push(new SideAction('EVENT_THREAD', 'LOAD_ALL'));
                }
                this.sms._sideAction.next(sideActions);
            });
        this.featureSourceSubs = this.ms.featureSource.subscribe((features: Feature[]) => {
            this.rawDataSource.addFeatures(features);
        });
        this.newDataReceivedSubs = this.sms._onNewDataReceived.subscribe((data: any[]) => {
            this.ms.getFeaturesFromRawData(<IRawData[]>data);
        });
        this.geoMarkerSourceSubs = this.ms.geoMarkerSource.subscribe((features: Feature[]) => {
            features.forEach(feat => {
                const featId: any = feat.getId();
                feat.set('pinned', this.ms.pinnedGeoMarker.getValue().indexOf(featId) !== -1);
            });
            this.removeUnpinnedMarker(this.geoMarkerSource);
            this.geoMarkerSource.addFeatures(features);
        });
        this.actionEmitterSubs = this.sms._onNewActionClicked.subscribe(action => {
            this.onActionReceived(action);
        });
        this.outsideFeatureSelectorSubs = this.sms._onNewDataSelected.subscribe((ids: string[]) => {
            if (ids && ids.length) {
                this.selectAndGoTo(ids[0]);
            }
        });
        this.newEventThreadSearchValueSubs = this.sms._onNewSearchValue.subscribe(value => this.onActionReceived('CLEAR_RAW_DATA_SOURCE'));
        this.zoomToLayerSubs = this.ms.zoomToLayer.subscribe(id => {
            const layerToZoom = this._map
                .getLayers()
                .getArray()
                .find(layer => layer.get('id') === id);
            if (layerToZoom && layerToZoom instanceof VectorLayer) {
                this._map.getView().fit((<VectorLayer>layerToZoom).getSource().getExtent());
            }
        });
        this.zoomToFeatureSubs = this.ms.zoomToFeature.subscribe((request: ZoomToFeatureRequest) => {
            const targetLayer: VectorLayer = request.targetLayer === 'geoMarkerLayer' ? this.geoMarkerLayer : this.featureLayer;
            const featureById: Feature = targetLayer.getSource().getFeatureById(request.featureId);
            if (featureById) {
                this._map.getView().fit(featureById.getGeometry().getExtent());
            }
        });
        this.pinnedGeoMarkerSubs = this.ms.pinnedGeoMarker.subscribe((markerIds: string[]) => {
            this.geoMarkerSource.getFeatures().forEach((feat: Feature) => {
                const featId: any = feat.getId();
                feat.set('pinned', markerIds.indexOf(featId) !== -1);
            });
        });
        this.initDessinTools();
    }

    ngOnDestroy() {
        if (this.featureSourceSubs) {
            this.featureSourceSubs.unsubscribe();
        }
        if (this.outsideFeatureSelectorSubs) {
            this.outsideFeatureSelectorSubs.unsubscribe();
        }
        if (this.layerSubs) {
            this.layerSubs.unsubscribe();
        }
        if (this.zoomToLayerSubs) {
            this.zoomToLayerSubs.unsubscribe();
        }
        if (this.actionEmitterSubs) {
            this.actionEmitterSubs.unsubscribe();
        }
        if (this.geoMarkerSourceSubs) {
            this.geoMarkerSourceSubs.unsubscribe();
        }
        if (this.zoomToFeatureSubs) {
            this.zoomToFeatureSubs.unsubscribe();
        }
        if (this.pinnedGeoMarkerSubs) {
            this.pinnedGeoMarkerSubs.unsubscribe();
        }
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }
        if (this.newDataReceivedSubs) {
            this.newDataReceivedSubs.unsubscribe();
        }
        if (this.newEventThreadSearchValueSubs) {
            this.newEventThreadSearchValueSubs.unsubscribe();
        }
    }

    private removeUnpinnedMarker(source: VectorSource) {
        const features: Feature[] = source.getFeatures();
        const filterFeat = features.filter(feat => !feat.get('pinned'));
        filterFeat.forEach(feat => {
            source.removeFeature(feat);
        });
    }

    private initMap() {
        // const readFeatures = new GeoJSON().readFeatures(GEO_JSON_OBJECT);
        this._map = new Map({
            layers: [this.featureLayer, this.geoMarkerLayer],
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
        setTimeout(() => {
            window.dispatchEvent(new Event('resize'));
        });
        this._map.addInteraction(this.selectInteraction);
        this._map.addInteraction(this.dragAndDropInteraction);

        this.dragAndDropInteraction.on('addfeatures', (event: DragAndDropInteraction.Event) => {
            const newMapLayer: MapLayer = new MapLayer(UUID(), 'KML', 'KML', true, 1);
            newMapLayer.layerStatus = 'UPDATE'; // Pour éviter de l'ajouter une deuxième fois
            const vectorSource = new VectorSource({
                features: event.features,
                wrapX: false
            });
            const newVectorLayer: VectorLayer = new VectorLayer({
                source: vectorSource,
                zIndex: newMapLayer.layerZIndex
            });
            vectorSource.set('id', newMapLayer.layerId);
            newVectorLayer.set('id', newMapLayer.layerId);
            this._map.addLayer(newVectorLayer);
            this._map.getView().fit(vectorSource.getExtent());
            this.ms.mapLayers.next(this.ms.mapLayers.getValue().concat(newMapLayer));
        });

        this.selectInteraction.on('select', (event: SelectInteration.Event) => {
            const selectedIds: any[] = this.selectInteraction
                .getFeatures()
                .getArray()
                .map(feat => {
                    return feat.getId();
                });
            this.sms._selectedData.next(selectedIds);
        });
    }

    private initMapLayerListener() {
        this.layerSubs = this.ms.mapLayers.subscribe((update: MapLayer[]) => {
            if (this._map.getLayers().getLength() === 1) {
                // Si aucun layer (autre que le VectorLayer de base) présent dans la map, alors tous les layers sont status NEW
                update.forEach(layer => (layer.layerStatus = 'NEW'));
            }
            this.updateLayers(update);
        });
    }

    updateLayers(layers: MapLayer[]) {
        const layerIds: string[] = layers.map(layer => layer.layerId);
        const updateLayers = {}; // Transformation en object pour permettre la recherche par key/value
        layers.filter(layer => layer.layerStatus === 'UPDATE').forEach(layer => (updateLayers[layer.layerId] = layer));

        // UPDATE
        this._map.getLayers().forEach(layer => {
            const layerId: string = layer.get('id');
            if (updateLayers[layerId]) {
                const mapLayer: MapLayer = updateLayers[layerId];
                layer.setVisible(mapLayer.visible);
                layer.setZIndex(mapLayer.layerZIndex);

                // Si changement de sélection du layer de dessin
                if (this.getMapStates().DESSIN_ENABLED && mapLayer.layerType === 'DESSIN' && mapLayer.selected) {
                    if (this.drawInteraction.get('id') !== mapLayer.layerId) {
                        this.removeDrawInteraction();
                        this.addDrawInteraction();
                    }
                }
            }
        });

        // DELETE
        const deleteLayer = [];
        this._map.getLayers().forEach(layer => {
            if (layer.get('id') && layerIds.indexOf(layer.get('id')) === -1) {
                deleteLayer.push(layer);
            }
        });
        deleteLayer.forEach(layer => this._map.removeLayer(layer));

        // NEW
        const addLayers: MapLayer[] = layers.filter(layer => layer.layerStatus === 'NEW');
        addLayers.forEach(newLayer => {
            let newItem = null;
            if (newLayer.layerType === 'DESSIN') {
                const vectorSource = new VectorSource({ wrapX: false });
                vectorSource.set('id', newLayer.layerId);
                newItem = new VectorLayer({
                    source: vectorSource,
                    style: (feature: Feature) => this.getDessinStyle(),
                    zIndex: newLayer.layerZIndex
                });
            } else if (newLayer.layerType === 'WMS') {
                if (newLayer.layerName === 'OSM') {
                    newItem = new TileLayer({
                        source: new OSM({ wrapX: false }),
                        zIndex: newLayer.layerZIndex
                    });
                } else if (newLayer.layerName === 'BingMaps') {
                    // key associée à un compte microsoft perso
                    newItem = new TileLayer({
                        source: new BingMaps({
                            key: 'AhZ8yD8nLNihhvRg-tAzuo49c2tqIkKLDKyYqCkMoQmniNx0ruDCDmq0kbR--sGl',
                            imagerySet: 'Aerial',
                            maxZoom: 19,
                            wrapX: false
                        }),
                        zIndex: newLayer.layerZIndex
                    });
                } else {
                    newItem = new TileLayer({
                        source: new TileWMS({
                            wrapX: false,
                            url: newLayer.properties['WMS_URL'],
                            crossOrigin: 'anonymous',
                            attributions: newLayer.properties['WMS_ATTRIBUTION']
                                ? newLayer.properties['WMS_ATTRIBUTION']
                                : newLayer.layerName,
                            params: { LAYERS: newLayer.properties['WMS_LAYERS_NAME'] }
                        }),
                        zIndex: newLayer.layerZIndex
                    });
                }
            }
            if (newItem !== null) {
                newItem.setVisible(newLayer.visible);
                newItem.set('id', newLayer.layerId);
                this._map.addLayer(newItem);
            }
        });
    }

    private initDessinTools() {
        // pairwise permet de recevoir les items sous la forme [oldValue, newValue],
        // startWith initialise la premiere value
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

    getSelectedDessinSource(): VectorSource {
        const selectedLayer: MapLayer = this.ms.mapLayers.getValue().find(layer => layer.layerType === 'DESSIN' && layer.selected);
        if (selectedLayer == null || typeof selectedLayer === 'undefined') {
            return null;
        }
        for (const layer of this._map.getLayers().getArray()) {
            if (layer instanceof VectorLayer && layer.get('id') === selectedLayer.layerId) {
                return (<VectorLayer>layer).getSource();
            }
        }
    }

    addDrawInteraction() {
        const currentDessinSrc: VectorSource = this.getSelectedDessinSource();
        if (currentDessinSrc == null) {
            return;
        }
        this.drawInteraction = new DrawInteraction({
            source: currentDessinSrc,
            type: this.getDessinStates().form === 'Rectangle' ? 'Circle' : this.getDessinStates().form,
            geometryFunction: this.getDessinStates().form === 'Rectangle' ? DrawInteraction.createBox() : null
        });
        this.drawInteraction.set('id', currentDessinSrc.get('id'));
        this._map.addInteraction(this.drawInteraction);
        this.snapInteraction = new SnapInteraction({
            source: currentDessinSrc
        });
        this._map.addInteraction(this.snapInteraction);
        this.modifyInteraction = new ModifyInteraction({
            source: currentDessinSrc
        });
        this._map.addInteraction(this.modifyInteraction);
    }

    removeDrawInteraction() {
        this._map.removeInteraction(this.drawInteraction);
        this._map.removeInteraction(this.snapInteraction);
        this._map.removeInteraction(this.modifyInteraction);
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
            this.selectInteraction.dispatchEvent({
                type: 'select',
                selected: [selectedFeat],
                deselected: [],
                mapBrowserEvent: null
            });
            this._map.getView().fit(selectedFeat.getGeometry().getExtent(), { duration: 1500 });
        }
    }

    getMapStates(): MapState {
        return this.ms.mapStates.getValue();
    }

    mainStyleFunction(feature: Feature, isSelected: boolean) {
        const mainStyle: Style = this.getStyle(feature.getGeometry().getType());
        if (isSelected) {
            mainStyle.setImage(this.selectedCircleImage);
        }
        // if (feature.getGeometry().getType() === 'Point') {
        //     const iconStyle: Style = this.getIconStyle(feature);
        //     if (iconStyle !== null) {
        //         const zoom = this._map.getView().getZoom();
        //         if (zoom > 5 && this.getMapStates().DISPLAY_LABEL && feature.get('label')) {
        //             mainStyle.getText().setText(feature.get('label'));
        //         } else {
        //             mainStyle.getText().setText('');
        //         }
        //         return [mainStyle, iconStyle];
        //     }
        // }
        if (feature.getGeometry().getType() === 'Point') {
            const iconStyle: Style = this.getIconStyle(feature, isSelected);
            if (iconStyle !== null) {
                const zoom = this._map.getView().getZoom();
                if (zoom > 5 && this.getMapStates().DISPLAY_LABEL && feature.get('label')) {
                    iconStyle.setText(
                        new Text({
                            font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
                            placement: 'point',
                            textBaseline: 'top',
                            offsetY: 10,
                            fill: new Fill({
                                color: 'black'
                            }),
                            text: feature.get('label')
                        })
                    );
                }
                return [iconStyle];
            }
        }
        return [mainStyle];
    }

    geoMarkerStyleFunction(feature: Feature, isSelected: boolean) {
        if (feature.getGeometry().getType() === 'Point') {
            const isPinned = feature.get('pinned');
            const iconStyle: Style = this.getIconStyle(feature, isPinned, 0.05);
            if (iconStyle !== null) {
                iconStyle.setText(
                    new Text({
                        font: 'bold 11px "Open Sans", "Arial Unicode MS", "sans-serif"',
                        placement: 'point',
                        textBaseline: 'top',
                        offsetY: 10,
                        fill: new Fill({
                            color: 'black'
                        }),
                        text: feature.get('label')
                    })
                );
                return [iconStyle];
            }
        }
        return this.getStyle(feature.getGeometry().getType());
    }

    getIconStyle(feature: Feature, selected?: boolean, scale?: number): Style {
        const objectType = feature.get('objectType');
        const src: string = selected ? MapService.getSelectedImageIconUrl(objectType) : MapService.getImageIconUrl(objectType);
        return src === null
            ? null
            : new Style({
                  image: new Icon({
                      anchor: [0.5, 0.5],
                      scale: scale ? scale : 0.01,
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
        let emitNewState = true;
        switch (action) {
            case 'F_ALL_DATA':
                if (mapState.FILTER_TYPE !== 'all') {
                    mapState.FILTER_TYPE = 'all';
                    this.clearRawDataSource();
                }
                break;
            case 'F_LOCATIONS_ONLY':
                if (mapState.FILTER_TYPE !== 'locations') {
                    mapState.FILTER_TYPE = 'locations';
                    this.clearRawDataSource();
                }
                break;
            case 'F_IMAGES_ONLY':
                if (mapState.FILTER_TYPE !== 'images') {
                    mapState.FILTER_TYPE = 'images';
                    this.clearRawDataSource();
                }
                break;
            case 'F_NO_FILTER':
                if (mapState.FILTER_TYPE !== '') {
                    mapState.FILTER_TYPE = '';
                    this.clearRawDataSource();
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
            case 'CLEAR_RAW_DATA_SOURCE':
                this.clearRawDataSource();
                emitNewState = false;
                break;
            case 'AUTO_REFRESH':
                mapState.AUTO_REFRESH = !mapState.AUTO_REFRESH;
                break;
            default:
                break;
        }
        if (emitNewState) {
            this.ms.mapStates.next(mapState);
        }
    }

    clearRawDataSource() {
        this.rawDataSource.clear();
    }
}
