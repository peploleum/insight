import { Component, OnInit } from '@angular/core';
import { MapService } from '../map.service';
import { Subscription } from 'rxjs/index';
import { MapState } from '../../shared/util/map-utils';
import { updateUniqueElementArray } from '../../shared/util/insight-util';
import { SideMediatorService } from '../../side/side-mediator.service';

@Component({
    selector: 'ins-content-filter',
    templateUrl: './content-filter.component.html',
    styles: []
})
export class ContentFilterComponent implements OnInit {
    mapStates: MapState;
    mapStatesSubs: Subscription;

    constructor(private _ms: MapService, private _sms: SideMediatorService) {}

    ngOnInit() {
        this.mapStatesSubs = this._ms.mapStates.subscribe(state => {
            this.mapStates = state;
        });
    }

    modifyState(action: string) {
        const currentState: MapState = this._ms.mapStates.getValue();
        switch (action) {
            case 'BIOGRAPHICS':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'Biographics');
                break;
            case 'EVENT':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'Event');
                break;
            case 'LOCATION':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'Location');
                break;
            case 'EQUIPMENT':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'Equipment');
                break;
            case 'ORGANISATION':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'Organisation');
                break;
            case 'RAWDATA':
                currentState.FILTER_ENTITIES = updateUniqueElementArray(currentState.FILTER_ENTITIES, 'RawData');
                break;
            case 'DISPLAY_RELATION':
                currentState.DISPLAY_RELATION = !currentState.DISPLAY_RELATION;
                break;
            default:
                break;
        }
        this._ms.mapStates.next(currentState);
        this.sendAction('RELOAD_SOURCE_FEATURE');
    }

    sendAction(action: string): void {
        this._sms._onNewActionClicked.next(action);
    }
}
