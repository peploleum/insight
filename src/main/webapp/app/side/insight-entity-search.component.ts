import { Component, OnInit } from '@angular/core';
import { getGenericNameProperty, getGenericSymbolProperty, toKebabCase, updateUniqueElementArray } from '../shared/util/insight-util';
import { GenericModel } from '../shared/model/generic.model';

@Component({
    selector: 'ins-insight-entity-search',
    templateUrl: './insight-entity-search.component.html',
    styles: []
})
export class InsightEntitySearchComponent implements OnInit {
    selectedEntityTypes: string[] = ['RawData'];
    queryResult: GenericModel[];

    constructor() {}

    ngOnInit() {}

    onAction(action: string) {
        switch (action) {
            case 'ADD_BIOGRAPHICS':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'Biographics');
                break;
            case 'ADD_EVENT':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'Event');
                break;
            case 'ADD_LOCATION':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'Location');
                break;
            case 'ADD_EQUIPMENT':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'Equipment');
                break;
            case 'ADD_ORGANISATION':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'Organisation');
                break;
            case 'ADD_RAWDATA':
                this.selectedEntityTypes = updateUniqueElementArray(this.selectedEntityTypes, 'RawData');
                break;
            default:
                break;
        }
    }

    onDataSelected(entity: GenericModel) {
        this.queryResult = [entity];
    }

    onResultQueryReceived(entities: GenericModel[]) {
        this.queryResult = entities;
    }

    getEntityNameProperty(entity: GenericModel): string {
        return getGenericNameProperty(entity);
    }

    getEntitySymbolProperty(entity: GenericModel): string {
        return getGenericSymbolProperty(entity);
    }

    getLink(str: string): string {
        const i: string = toKebabCase(str);
        return '/' + i;
    }
}
