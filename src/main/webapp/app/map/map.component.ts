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
import Cluster from 'ol/source/cluster';
import MapBrowserEvent from 'ol/mapbrowserevent';

import Stroke from 'ol/style/stroke';
import Circle from 'ol/style/circle';
import Icon from 'ol/style/icon';
import Style from 'ol/style/style';
import Fill from 'ol/style/fill';
import Text from 'ol/style/text';
import {
    FigureStyle,
    getMapImageIconUrl,
    getSelectedImageIconUrl,
    insBaseStyleFunction,
    insHoveredStyleFunction,
    insSelectedStyleFunction,
    insSetTextStyleFunction,
    insStyleFunction,
    MapLayer,
    MapState,
    setClusterRadius,
    ZoomToFeatureRequest
} from '../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { pairwise, startWith } from 'rxjs/internal/operators';
import { ToolbarButtonParameters, UUID } from '../shared/util/insight-util';
import { SideMediatorService } from '../side/side-mediator.service';
import { EventThreadParameters, SideAction, SideParameters, ToolbarState } from '../shared/util/side.util';
import { GenericModel } from '../shared/model/generic.model';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styles: [':host { flex-grow: 1 }']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
    featureSource: VectorSource = new VectorSource({ wrapX: false });
    geoMarkerSource: VectorSource = new VectorSource({ wrapX: false });
    clusterSource: Cluster;
    featureLayer: VectorLayer;
    geoMarkerLayer: VectorLayer;
    _map: Map;

    hoverInteraction: SelectInteration;
    drawInteraction: DrawInteraction;
    snapInteraction: SnapInteraction;
    modifyInteraction: ModifyInteraction;
    dragAndDropInteraction: DragAndDropInteraction;

    computedHeight = 0;

    currentResolution: number;
    maxClusterCount: number;
    selectedIds: any[] = [];

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

    constructor(
        private er: ElementRef,
        private cdr: ChangeDetectorRef,
        private ms: MapService,
        private sms: SideMediatorService,
        private _activatedRoute: ActivatedRoute
    ) {
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
                const styles: Style[] = insStyleFunction(feature, resolution, this.maxClusterCount || 1);
                const zoom = this._map.getView().getZoom();
                if (zoom > 5 && this.getMapStates().DISPLAY_LABEL) {
                    insSetTextStyleFunction(feature, styles);
                }
                return styles;
            },
            zIndex: 1
        });
        this.geoMarkerLayer = new VectorLayer({
            source: this.geoMarkerSource,
            style: (feature: Feature) => this.geoMarkerStyleFunction(feature, false),
            zIndex: 1
        });

        this.hoverInteraction = new SelectInteration({
            condition: evt => {
                return evt.type === 'pointermove';
            },
            filter: (feature: Feature, layer: VectorLayer) => {
                return feature.get('features').length > 1;
            },
            style: (feature: Feature, resolution: number) => {
                return insHoveredStyleFunction(feature, resolution, this.maxClusterCount || 1);
            },
            multi: true
        });
        this.dragAndDropInteraction = new DragAndDropInteraction({
            formatConstructors: [KML],
            projection: 'EPSG:3857'
        });
    }

    internalOnResize() {
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
            this.featureSource.addFeatures(features);
        });
        this.newDataReceivedSubs = this.sms._onNewDataReceived.subscribe((data: any[]) => {
            this.ms.getFeaturesFromGeneric(<GenericModel[]>data);
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

        this._activatedRoute.data.subscribe(({ inputData }) => {
            if (inputData) {
                const data: GenericModel[] = <GenericModel[]>inputData;
                this.ms.getFeaturesFromGeneric(data);
            }
        });
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
        // this._map.addInteraction(this.selectInteraction);
        this._map.addInteraction(this.hoverInteraction);
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

        this._map.on('singleclick', (event: MapBrowserEvent) => {
            if (!this.getMapStates().DESSIN_ENABLED) {
                const feat: any = this._map.getFeaturesAtPixel(event.pixel);
                const feats: Feature[] = feat ? (Array.isArray(feat) ? feat : [feat]) : [];
                this.onNewFeaturesSelected(feats);
            }
            event.preventDefault();
        });
    }

    private onNewFeaturesSelected(features: Feature[]) {
        this.selectedIds.forEach(id => {
            this.featureSource.getFeatureById(id).set('selected', false);
        });
        this.selectedIds =
            features && features.length > 0
                ? features
                      .map(feat => (<Feature[]>feat.get('features')).map(f => f.getId()))
                      .reduce((x, y) => {
                          return x.concat(y);
                      }, [])
                : [];
        if (features) {
            features.forEach((feat: Feature) => {
                feat.set('selected', true);
                feat.get('features').forEach(f => f.set('selected', true));
            });
        }
        this.sms._selectedData.next(this.selectedIds);
    }

    private initMapLayerListener() {
        this.layerSubs = this.ms.mapLayers.subscribe((update: MapLayer[]) => {
            console.log(this._map.getLayers().getLength());
            // 2 correspond au 2 layers ajoutés obligatoirement (featureLayer et geoMarkerLayer) dans la fonction initMap
            if (this._map.getLayers().getLength() === 2) {
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
        /*this.selectInteraction.getFeatures().clear();
         const selectedFeat: Feature = this.featureSource.getFeatureById(objectId);
         if (selectedFeat) {
         this.selectInteraction.getFeatures().push(selectedFeat);
         this.selectInteraction.dispatchEvent({
         type: 'select',
         selected: [selectedFeat],
         deselected: [],
         mapBrowserEvent: null
         });
         this._map.getView().fit(selectedFeat.getGeometry().getExtent(), {duration: 1500});
         }*/

        const selectedFeat: Feature = this.featureSource.getFeatureById(objectId);
        if (selectedFeat) {
            this.onNewFeaturesSelected([selectedFeat]);
            this._map.getView().fit(selectedFeat.getGeometry().getExtent(), { duration: 1500 });
        }
    }

    getMapStates(): MapState {
        return this.ms.mapStates.getValue();
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
        return insBaseStyleFunction(feature.getGeometry().getType());
    }

    getIconStyle(feature: Feature, selected?: boolean, scale?: number): Style {
        const objectType = feature.get('objectType');
        const src: string = selected ? getSelectedImageIconUrl(objectType) : getMapImageIconUrl(objectType);
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
                    // this._map.addInteraction(this.selectInteraction);
                } else if (mapState.DESSIN_ENABLED) {
                    // this._map.removeInteraction(this.selectInteraction);
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
        this.featureSource.clear();
    }
}
