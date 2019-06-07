import { Component, OnDestroy, OnInit } from '@angular/core';
import { MapService } from '../map.service';
import { Subscription } from 'rxjs/index';
import { MapState } from '../../shared/util/map-utils';
import { BASE64URI, updateUniqueElementArray } from '../../shared/util/insight-util';
import { SideMediatorService } from '../../side/side-mediator.service';
import { MapSchema } from '../../shared/model/map.model';
import { GenericModel } from '../../shared/model/generic.model';
import { QuickViewService } from '../../side/quick-view.service';
import { map } from 'rxjs/internal/operators';
import { HttpResponse } from '@angular/common/http';
import { IGraphStructureNodeDTO } from '../../shared/model/node.model';

@Component({
    selector: 'ins-schema-info',
    templateUrl: './schema-info.component.html',
    styles: []
})
export class SchemaInfoComponent implements OnInit, OnDestroy {
    mapStates: MapState;
    mapSchema: MapSchema;
    schemaEntityRoot: GenericModel;

    mapStatesSubs: Subscription;
    mapSchemaSubs: Subscription;

    constructor(private _ms: MapService, private _sms: SideMediatorService, private _qvs: QuickViewService) {}

    ngOnInit() {
        this.mapStatesSubs = this._ms.mapStates.subscribe(state => {
            let needReload = false;
            if (this.schemaEntityRoot && this.mapStates.MAX_GRAPH_DEPTH !== state.MAX_GRAPH_DEPTH) {
                // Reload schema si la profondeur est modifiée
                needReload = true;
            }
            this.mapStates = Object.assign({}, state);
            if (needReload) {
                this.onEntitySelected(this.schemaEntityRoot);
            }
        });
        this.mapSchemaSubs = this._ms.mapSchema.subscribe(schema => {
            this.mapSchema = schema;
            this.getEntityRoot();
        });
    }

    ngOnDestroy() {
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }
        if (this.mapSchemaSubs) {
            this.mapSchemaSubs.unsubscribe();
        }
    }

    onEntitySelected(entity: GenericModel) {
        this._qvs
            .getGraphForEntity(entity['externalId'], this.mapStates.MAX_GRAPH_DEPTH)
            .pipe(map((res: HttpResponse<IGraphStructureNodeDTO>) => res.body))
            .subscribe(graph => {
                this._ms.mapSchema.next(new MapSchema(graph));
            });
    }

    getEntityRoot() {
        if (this.mapSchema) {
            this._qvs
                .find(this.mapSchema.hierarchicContent.nodeId)
                .pipe(map((res: HttpResponse<GenericModel>) => res.body))
                .subscribe(entity => (this.schemaEntityRoot = entity));
        }
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    modifyState(action: string) {
        const currentState: MapState = this._ms.mapStates.getValue();
        let needReload = true;
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
            case 'MAX_GRAPH_DEPTH':
                currentState.MAX_GRAPH_DEPTH = currentState.MAX_GRAPH_DEPTH === 2 ? 1 : 2;
                needReload = false;
                break;
            default:
                break;
        }
        this._ms.mapStates.next(currentState);
        if (needReload) {
            this.sendAction('RELOAD_SOURCE_FEATURE');
        }
    }

    sendAction(action: string): void {
        this._sms._onNewActionClicked.next(action);
    }
}
