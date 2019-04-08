import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/internal/operators';
import { SearchService } from './search.service';
import { GenericModel } from '../model/quick-view.model';
import { InsightSearchDirective } from './insight-search.directive';

@Component({
    selector: 'ins-insight-search',
    templateUrl: './insight-search.component.html',
    styleUrls: ['insight-search.scss']
})
export class InsightSearchComponent implements OnInit {
    @Input()
    targetEntity: string;
    @Input()
    textFields: string[];
    @Input()
    symbolField: string;
    searchForm: FormControl = new FormControl('');

    suggestions: GenericModel[];

    @ViewChild(InsightSearchDirective) suggestDirective: InsightSearchDirective;

    constructor(private _ss: SearchService) {}

    ngOnInit() {
        this.searchForm.valueChanges
            .pipe(
                debounceTime(300),
                distinctUntilChanged(),
                switchMap((value: string) => {
                    return this._ss.search(this.targetEntity, value);
                })
            )
            .subscribe((entities: GenericModel[]) => {
                this.suggestions = entities;
                this.displaySuggestions(!(!entities || entities.length === 0));
            });
    }

    displaySuggestions(display: boolean) {
        if (this.suggestDirective) {
            if (display) {
                this.suggestDirective.suggestions = this.suggestions;
                this.suggestDirective.display();
            } else {
                this.suggestDirective.hide();
            }
        }
    }
}
