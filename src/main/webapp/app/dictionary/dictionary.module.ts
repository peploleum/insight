import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InsightSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { dictionaryRoute } from 'app/dictionary/dictionary.route';
import { DictionaryComponent } from './dictionary.component';
import { DictionaryEditorComponent } from './dictionary-editor/dictionary-editor.component';
import { DictionaryTableComponent } from './dictionary-table/dictionary-table.component';

const ENTITY_STATES = [...dictionaryRoute];

@NgModule({
    declarations: [DictionaryComponent, DictionaryEditorComponent, DictionaryTableComponent],
    imports: [CommonModule, InsightSharedModule, RouterModule.forChild(ENTITY_STATES)]
})
export class DictionaryModule {}
