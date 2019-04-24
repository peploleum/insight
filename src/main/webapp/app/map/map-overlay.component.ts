/**
 * Created by gFolgoas on 24/04/2019.
 */
import { AfterViewInit, Component, EventEmitter, Inject, OnDestroy, OnInit } from '@angular/core';
import { QuickViewService } from '../side/quick-view.service';
import { GenericModel } from '../shared/model/generic.model';
import { BASE64URI, getDataTypeIcon, getSourceTypeIcon } from '../shared/util/insight-util';
import { MapOverlayGenericMapper } from '../shared/util/map-utils';

@Component({
    selector: 'ins-map-overlay',
    templateUrl: './map-overlay.component.html',
    styleUrls: ['map-overlay.component.scss']
})
export class MapOverlayComponent implements OnInit, OnDestroy, AfterViewInit {
    entity: GenericModel;
    mapper: MapOverlayGenericMapper;

    constructor(
        @Inject('popupInteract') private _popupInteract: EventEmitter<string>,
        @Inject('idList') private _selectedIds: string[],
        private _qvs: QuickViewService
    ) {}

    ngOnInit() {
        if (this._selectedIds && this._selectedIds.length > 0) {
            this._qvs.find(this._selectedIds[0]).subscribe(res => {
                this.entity = res.body;
                this.mapper = MapOverlayGenericMapper.fromGeneric(this.entity);
            });
        }
    }

    ngAfterViewInit() {}

    ngOnDestroy() {}

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    getDataTypeIcon(type: string): string {
        return getDataTypeIcon(type);
    }

    getSourceTypeIcon(type: string): string {
        return getSourceTypeIcon(type);
    }
}
