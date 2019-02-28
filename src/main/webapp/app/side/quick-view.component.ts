import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideMediatorService } from './side-mediator.service';
import { QuickViewService } from './quick-view.service';
import { GenericModel } from '../shared/model/quick-view.model';
import { ToolbarButtonParameters } from '../shared/util/insight-util';
import { Subscription } from 'rxjs/index';
import { ToolbarState } from '../shared/util/side.util';
import { map } from 'rxjs/internal/operators';

@Component({
    selector: 'ins-quick-view',
    templateUrl: './quick-view.component.html',
    styles: []
})
export class QuickViewComponent implements OnInit, OnDestroy {
    entity: GenericModel;
    toolbar_actions: ToolbarButtonParameters[] = [];
    selected_data_ids: string[] = [];

    toolbarActionsSubs: Subscription;
    selectedDataSubs: Subscription;

    constructor(private _sms: SideMediatorService, private _qvs: QuickViewService) {}

    ngOnInit() {
        setTimeout(() => {
            this.toolbarActionsSubs = this._sms._toolbarActions
                .pipe(map((states: ToolbarState[]) => states.filter(state => state.componentTarget === 'QUICK_VIEW')))
                .subscribe((states: ToolbarState[]) => {
                    states.forEach(actions => (this.toolbar_actions = actions.parameters));
                });
        });
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
        if (this.toolbarActionsSubs) {
            this.toolbarActionsSubs.unsubscribe();
        }
        if (this.selectedDataSubs) {
            this.selectedDataSubs.unsubscribe();
        }
    }

    getEntity(id: string) {
        id = '5c6150271605a829c8677dea';
        this._qvs.find(id).subscribe(
            res => {
                this.entity = res.body;
            },
            error => console.log('Error finding entity by id')
        );
    }

    onAction(action: string) {
        switch (action) {
            case 'PREVIOUS':
                const currentIndex = this.selected_data_ids.indexOf(this.entity['id']);
                if (this.selected_data_ids[currentIndex - 1]) {
                    this.getEntity(this.selected_data_ids[currentIndex - 1]);
                }
                break;
            case 'NEXT':
                const currentIndex2 = this.selected_data_ids.indexOf(this.entity['id']);
                if (this.selected_data_ids[currentIndex2 + 1]) {
                    this.getEntity(this.selected_data_ids[currentIndex2 + 1]);
                }
                break;
            default:
                break;
        }
    }
}
