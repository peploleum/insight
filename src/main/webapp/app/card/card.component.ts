import { Component, OnInit } from '@angular/core';
import { GenericModel } from '../shared/model/generic.model';
import { BASE64URI } from '../shared/util/insight-util';
import { QuickViewService } from '../side/quick-view.service';
import { Observable } from 'rxjs/index';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/internal/operators';

@Component({
    selector: 'ins-card',
    templateUrl: './card.component.html',
    styles: []
})
export class CardComponent implements OnInit {
    entity: GenericModel;
    entityExternalId: string;
    entityAndNeighbors: GenericModel[];
    currentTab = 'info';

    constructor(private _qv: QuickViewService) {}

    ngOnInit() {}

    onDataSelected(entity: GenericModel) {
        this.entity = entity;
        this.getImage(this.entity['id']);
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

    getImage(id: string) {
        this._qv
            .find(id)
            .pipe(map((res: HttpResponse<GenericModel>) => res.body))
            .subscribe(entity => {
                this.entity['biographicsImage'] = entity['biographicsImage'];
                this.entity['biographicsSymbol'] = entity['biographicsSymbol'];
            });
    }
}
