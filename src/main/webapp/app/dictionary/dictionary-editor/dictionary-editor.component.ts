import { Component, Input, OnInit } from '@angular/core';
import { ThemeDTO } from 'app/shared/model/analytics.model';
import { IDictionaryContainer } from 'app/dictionary/dictionary.utils';

@Component({
    selector: 'ins-dictionary-editor',
    templateUrl: './dictionary-editor.component.html',
    styleUrls: ['dictionary-editor.component.scss']
})
export class DictionaryEditorComponent implements OnInit {
    private _selectedDictionary: IDictionaryContainer;
    selectedTheme: ThemeDTO;

    constructor() {}

    ngOnInit() {}

    @Input()
    set selectedDictionary(value: IDictionaryContainer) {
        if (value && value.dictionary) {
            this._selectedDictionary = value;
            this.selectedTheme = null;
            if (this._selectedDictionary.dictionary.theme) {
                this.selectedTheme = this._selectedDictionary.dictionary.theme[0];
            }
        }
    }

    get selectedDictionary(): IDictionaryContainer {
        return this._selectedDictionary;
    }

    onClick(theme: ThemeDTO) {
        this.selectedTheme = theme;
    }
}
