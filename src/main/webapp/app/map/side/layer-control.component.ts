/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';
import { getlayerIcon, MapLayer } from '../../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { map } from 'rxjs/internal/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UUID } from '../../shared/util/insight-util';

@Component({
    selector: 'ins-layer-control',
    templateUrl: './layer-control.component.html'
})
export class LayerControlComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    layerList: MapLayer[];
    layersSubs: Subscription;

    wmsLayerForm: FormGroup;

    selectedLayer: MapLayer;

    constructor(protected ms: MapService, protected formBuilder: FormBuilder) {
        super();
    }

    ngOnInit() {
        this.layersSubs = this.ms.mapLayers
            .pipe(map((layers: MapLayer[]) => layers.filter(layer => layer.layerType === 'WMS')))
            .subscribe(layers => {
                this.layerList = layers;
            });
    }

    ngAfterViewInit() {}

    ngOnDestroy() {}

    onSelectedLayerChanged(layer: MapLayer) {
        this.selectedLayer = layer;
    }

    displayLayerCreationForm(display: boolean) {
        if (display) {
            this.wmsLayerForm = this.formBuilder.group({
                wmsName: ['', Validators.required],
                wmsUrl: ['', Validators.required],
                wmsLayer: ['', Validators.required],
                wmsAttribution: ['']
            });
        } else {
            this.wmsLayerForm = null;
        }
    }

    getLayerIcon(layerType: string): string {
        return getlayerIcon(layerType);
    }

    onLayerAction(action: string, targetLayer?: MapLayer) {
        const currentLayerList: MapLayer[] = this.layers();
        currentLayerList.forEach(layer => (layer.layerStatus = 'UPDATE'));
        switch (action) {
            case 'VISIBILITY_CHANGED':
                if (targetLayer) {
                    targetLayer.visible = !targetLayer.visible;
                }
                break;
            case 'REMOVE_WMS':
                if (targetLayer) {
                    currentLayerList.splice(currentLayerList.indexOf(targetLayer), 1);
                }
                break;
            case 'ADD_WMS':
                if (this.wmsLayerForm && this.wmsLayerForm.valid) {
                    const formValue = this.wmsLayerForm.value;
                    const wmsProperties = {
                        WMS_URL: formValue.wmsUrl,
                        WMS_ATTRIBUTION: formValue.wmsAttribution,
                        WMS_LAYERS_NAME: formValue.wmsLayer
                    };
                    currentLayerList.push(new MapLayer(UUID(), formValue.wmsName, 'WMS', true, 0, true, wmsProperties));
                }
                break;
            case 'MOVE_LAYER_DOWN':
                if (targetLayer) {
                    targetLayer.layerZIndex--;
                }
                break;
            case 'MOVE_LAYER_UP':
                if (targetLayer) {
                    targetLayer.layerZIndex++;
                }
                break;
            default:
                break;
        }
        this.ms.mapLayers.next(currentLayerList);
    }

    layers() {
        return this.ms.mapLayers.getValue();
    }
}
