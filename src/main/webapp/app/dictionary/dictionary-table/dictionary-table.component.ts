import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IDictionary } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-dictionary-table',
    templateUrl: './dictionary-table.component.html',
    styles: []
})
export class DictionaryTableComponent implements OnInit {
    @Input()
    dictionaries: IDictionary[];
    @Input()
    selectedDictionary: IDictionary;

    @Output()
    selectionEmitter: EventEmitter<IDictionary> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    onClick(dico: IDictionary) {
        this.selectionEmitter.emit(dico);
    }
}
