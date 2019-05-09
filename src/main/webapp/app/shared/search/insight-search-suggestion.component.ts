import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { GenericModel } from '../model/generic.model';
import { BASE64URI, getGenericImageProperty, getGenericNameProperty, SYMBOL_URLS } from '../util/insight-util';
import { NetworkService } from '../../network/network.service';

@Component({
    selector: 'ins-insight-search-suggestion',
    templateUrl: './insight-search-suggestion.component.html',
    styleUrls: ['insight-search.scss']
})
export class InsightSearchSuggestionComponent implements OnInit {
    suggestions: GenericModel[];
    textFields: string[];
    imageField: string;
    dimension: { top: number; left: number; width: number } = { top: 0, left: 0, width: 0 };

    constructor(@Inject('directiveSelectionEmitter') private _selectionEmitter: EventEmitter<any>) {}

    ngOnInit() {}

    getBase64(content: string): string {
        return BASE64URI(content);
    }

    getDefaultEntitySymbol(entity: GenericModel): string {
        return NetworkService.getNodeImageUrl(entity['entityType']);
    }

    onClick(selectedEntity: GenericModel) {
        this._selectionEmitter.emit(selectedEntity);
    }

    getTextFieldProperties(entity: GenericModel): string[] {
        return this.textFields || [getGenericNameProperty(entity)];
    }

    getEntityImageProperty(entity: GenericModel): string {
        return this.imageField || getGenericImageProperty(entity);
    }
}
