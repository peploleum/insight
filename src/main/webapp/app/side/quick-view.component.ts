import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideMediatorService } from './side-mediator.service';
import { QuickViewService } from './quick-view.service';
import { GenericModel } from '../shared/model/quick-view.model';
import { Subscription } from 'rxjs/index';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'ins-quick-view',
    templateUrl: './quick-view.component.html',
    styles: []
})
export class QuickViewComponent implements OnInit, OnDestroy {
    entity: Map<string, string>;
    selected_data_ids: string[] = [];

    selectedDataSubs: Subscription;

    constructor(private _sms: SideMediatorService, private _qvs: QuickViewService) {}

    ngOnInit() {
        this.selectedDataSubs = this._sms._selectedData.subscribe((ids: string[]) => {
            this.selected_data_ids = ids;
            if (this.selected_data_ids && this.selected_data_ids.length) {
                this.getEntity(this.selected_data_ids[0]);
            } else {
                this.entity = null;
            }
        });
    }

    ngOnDestroy() {
        if (this.selectedDataSubs) {
            this.selectedDataSubs.unsubscribe();
        }
    }

    getEntity(id: string) {
        this._qvs.find(id).subscribe(
            (res: HttpResponse<GenericModel>) => {
                const map: Map<string, string> = new Map();
                Object.keys(res.body).forEach(key => {
                    map.set(key, res.body[key]);
                });
                this.entity = map;
            },
            error => console.log('Error finding entity by id')
        );
    }

    onAction(action: string) {
        switch (action) {
            case 'PREVIOUS':
                const currentIndex = this.selected_data_ids.indexOf(this.entity.get('id'));
                if (this.selected_data_ids[currentIndex - 1]) {
                    this.getEntity(this.selected_data_ids[currentIndex - 1]);
                }
                break;
            case 'NEXT':
                const currentIndex2 = this.selected_data_ids.indexOf(this.entity.get('id'));
                if (this.selected_data_ids[currentIndex2 + 1]) {
                    this.getEntity(this.selected_data_ids[currentIndex2 + 1]);
                }
                break;
            default:
                break;
        }
    }
}
