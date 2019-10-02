import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IDictionaryContainer } from 'app/dictionary/dictionary.utils';

@Component({
    selector: 'ins-dictionary-table',
    templateUrl: './dictionary-table.component.html',
    styleUrls: ['dictionary-editor.component.scss']
})
export class DictionaryTableComponent implements OnInit {
    @Input()
    dictionaries: IDictionaryContainer[];
    @Output()
    actionEmitter: EventEmitter<{ action: 'DELETE' | 'SAVE'; dico: IDictionaryContainer }> = new EventEmitter();

    constructor() {}

    ngOnInit() {}

    onClick(dico: IDictionaryContainer) {
        this.dictionaries.forEach(d => (d.selected = false));
        dico.selected = true;
    }

    deleteDictionary(dico: IDictionaryContainer) {
        this.actionEmitter.emit({ dico, action: 'DELETE' });
    }

    saveDictionary(dico: IDictionaryContainer) {
        this.actionEmitter.emit({ dico, action: 'SAVE' });
    }
}
