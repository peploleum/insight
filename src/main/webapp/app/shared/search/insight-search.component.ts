import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/internal/operators';
import { SearchService } from './search.service';
import { Observable } from 'rxjs/index';

@Component({
    selector: 'ins-insight-search',
    templateUrl: './insight-search.component.html',
    styles: []
})
export class InsightSearchComponent implements OnInit {
    @Input() targetEntity: string;
    searchForm: FormControl = new FormControl('');

    suggestion$: Observable<string[]>;

    constructor(private _ss: SearchService) {}

    ngOnInit() {
        this.suggestion$ = this.searchForm.valueChanges.pipe(
            debounceTime(100),
            distinctUntilChanged(),
            switchMap((value: string) => {
                return this._ss.autoComplete(this.targetEntity, value);
            })
        );
    }
}
