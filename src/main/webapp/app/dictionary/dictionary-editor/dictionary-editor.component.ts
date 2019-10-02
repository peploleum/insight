import { Component, Input, OnInit } from '@angular/core';
import { IDictionary, ThemeDTO } from 'app/shared/model/analytics.model';

@Component({
    selector: 'ins-dictionary-editor',
    templateUrl: './dictionary-editor.component.html',
    styleUrls: ['dictionary-editor.component.scss']
})
export class DictionaryEditorComponent implements OnInit {
    private _selectedDictionary: IDictionary;
    selectedTheme: ThemeDTO;

    constructor() {}

    ngOnInit() {}

    @Input()
    set selectedDictionary(value: IDictionary) {
        this._selectedDictionary = value;
        if (this._selectedDictionary && this._selectedDictionary.theme.length) {
            this.selectedTheme = this._selectedDictionary.theme[0];
        }
    }

    get selectedDictionary(): IDictionary {
        return this._selectedDictionary;
    }

    onClick(theme: ThemeDTO) {
        this.selectedTheme = theme;
    }
}
