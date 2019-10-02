import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IDictionary } from 'app/shared/model/analytics.model';
import { FileReaderEventTarget } from 'app/shared/util/insight-util';
import { IDictionaryContainer } from 'app/dictionary/dictionary.utils';
import { DictionaryService } from 'app/dictionary/dictionary.service';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'ins-dictionary',
    templateUrl: './dictionary.component.html',
    styleUrls: ['dictionary.component.scss']
})
export class DictionaryComponent implements OnInit {
    dictionaries: IDictionaryContainer[] = [];

    constructor(private _ar: ActivatedRoute, private _ds: DictionaryService) {
        this._ar.data.subscribe(({ dictionaries }) => {
            if (dictionaries) {
                this.dictionaries = (dictionaries as IDictionary[]).map(dico => {
                    return {
                        dictionary: dico,
                        selected: false,
                        modified: false
                    };
                });
            }
        });
    }

    ngOnInit() {}

    onFileInputChange(event: any) {
        if (event.target.files && event.target.files.length) {
            const [file] = event.target.files;
            this.extractJsonData(file);
        }
    }

    extractJsonData(json: File) {
        const reader: FileReader = new FileReader();
        let jsonString: string;
        reader.onload = (event: Event) => {
            jsonString = (<FileReaderEventTarget>event.target).result;
            const dictionary: IDictionary = JSON.parse(jsonString);
            this.dictionaries.push({
                dictionary,
                selected: false,
                modified: true
            });
        };
        reader.readAsText(json);
    }

    getSelectedDictionary(): IDictionaryContainer {
        return this.dictionaries.find(d => d.selected);
    }

    onAction(event: { action: 'DELETE' | 'SAVE'; dico: IDictionaryContainer }) {
        switch (event.action) {
            case 'SAVE':
                if (event.dico.dictionary.id) {
                    this._ds.update(event.dico.dictionary).subscribe(res => {
                        event.dico.dictionary = res.body;
                        event.dico.selected = false;
                        event.dico.modified = false;
                    });
                } else {
                    this._ds.create(event.dico.dictionary).subscribe(res => {
                        event.dico.dictionary = res.body;
                        event.dico.selected = false;
                        event.dico.modified = false;
                    });
                }
                break;
            case 'DELETE':
                if (event.dico.dictionary.id) {
                    this._ds.delete(event.dico.dictionary.id).subscribe(res => {
                        const idToDelete = this.dictionaries.findIndex(d => d.dictionary.id === event.dico.dictionary.id);
                        this.dictionaries.splice(idToDelete, 1);
                    });
                } else {
                    const idToDelete = this.dictionaries.findIndex(d => d === event.dico);
                    this.dictionaries.splice(idToDelete, 1);
                }
                break;
        }
    }

    refreshDictionaryList() {
        this._ds.getAll().subscribe((res: HttpResponse<IDictionary[]>) => {
            const modifiedDicos = this.dictionaries.filter(d => d.modified);
            const dictionaries = res.body
                .filter(x => {
                    const found = !!modifiedDicos.find(y => y.dictionary.id && y.dictionary.id === x.id);
                    return !found;
                })
                .map(x => {
                    return {
                        dictionary: x,
                        selected: false,
                        modified: false
                    };
                });
            this.dictionaries = modifiedDicos.concat(dictionaries);
        });
    }
}
