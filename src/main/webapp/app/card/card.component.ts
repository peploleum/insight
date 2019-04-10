import { Component, OnInit } from '@angular/core';
import { GenericModel } from '../shared/model/generic.model';
import { BASE64URI } from '../shared/util/insight-util';
import { QuickViewService } from '../side/quick-view.service';

@Component({
    selector: 'ins-card',
    templateUrl: './card.component.html',
    styles: []
})
export class CardComponent implements OnInit {
    entity: GenericModel;
    entityExternalId: string;
    entityAndNeighbors: GenericModel[];

    constructor(private _qv: QuickViewService) {}

    ngOnInit() {}

    onDataSelected(entity: GenericModel) {
        this.entity = entity;
        this.entityAndNeighbors = [this.entity];
        this.entityExternalId = this.entity['externalId'];
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    setEntities(ids: string[]) {
        this._qv.findMultiple(ids).subscribe(res => {
            this.entityAndNeighbors = res.body;
        });
    }
}
