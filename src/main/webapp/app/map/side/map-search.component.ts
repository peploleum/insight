/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/internal/operators';
import { IMapDataDTO } from '../../shared/model/map.model';

@Component({
    selector: 'ins-map-search',
    templateUrl: './map-search.component.html'
})
export class MapSearchComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    currentResult: IMapDataDTO[] = [];
    searchForm: FormControl = new FormControl('');

    constructor(protected ms: MapService) {
        super();
    }

    ngOnInit() {
        this.searchForm.valueChanges
            .pipe(
                debounceTime(400),
                distinctUntilChanged(),
                switchMap((value: string) => {
                    return this.ms.getGeoMarker(value);
                })
            )
            .subscribe((result: IMapDataDTO[]) => {
                this.ms.getFeaturesFromGeoMarker(result);
                this.currentResult = result;
            });
    }

    ngAfterViewInit() {}

    ngOnDestroy() {}
}
