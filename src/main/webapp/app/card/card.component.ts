import { Component, OnInit } from '@angular/core';
import { GenericModel } from '../shared/model/quick-view.model';
import { BASE64URI } from '../shared/util/insight-util';

@Component({
    selector: 'ins-card',
    templateUrl: './card.component.html',
    styles: []
})
export class CardComponent implements OnInit {
    entity: GenericModel;

    constructor() {}

    ngOnInit() {}

    onDataSelected(entity: GenericModel) {
        this.entity = entity;
    }

    getBase64(content: string): string {
        return BASE64URI(content);
    }
}
