import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IDictionary } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-dictionary',
    templateUrl: './dictionary.component.html',
    styleUrls: ['dictionary.component.scss']
})
export class DictionaryComponent implements OnInit {
    dictionaries: IDictionary[];
    selectedDictionary: IDictionary;

    constructor(private _ar: ActivatedRoute) {
        this._ar.data.subscribe(({ dictionaries }) => {
            if (dictionaries) {
                this.dictionaries = dictionaries;
            }
        });
    }

    ngOnInit() {}

    onDictionarySelected(dico: IDictionary) {
        this.selectedDictionary = dico;
    }
}
