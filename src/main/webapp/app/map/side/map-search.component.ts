/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from '../../side/side.abstract';
import { MapService } from '../map.service';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs/internal/operators';
import { IMapDataDTO } from '../../shared/model/map.model';
import { MapState, ZoomToFeatureRequest } from '../../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { SearchService } from '../../shared/search/search.service';
import { GenericModel } from '../../shared/model/generic.model';

@Component({
    selector: 'ins-map-search',
    templateUrl: './map-search.component.html'
})
export class MapSearchComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    currentResult: IMapDataDTO[] = [];
    searchForm: FormControl = new FormControl('');
    pinnedIds: string[];

    mapStates: MapState;
    mapStatesSubs: Subscription;

    pinnedGeoMarkerSubs: Subscription;

    constructor(protected ms: MapService, private _ss: SearchService) {
        super();
    }

    ngOnInit() {
        this.mapStatesSubs = this.ms.mapStates.subscribe(state => {
            this.mapStates = state;
        });
        this.searchForm.valueChanges
            .pipe(
                filter(val => this.mapStates.SEARCH_GEOREF),
                debounceTime(400),
                distinctUntilChanged(),
                switchMap((value: string) => {
                    return this.ms.getGeoMarker(value);
                })
            )
            .subscribe(
                (result: IMapDataDTO[]) => {
                    // Garde les pinned points
                    const filteredResult = this.currentResult.filter(item => this.ms.pinnedGeoMarker.getValue().indexOf(item.id) !== -1);
                    // Remove doublon
                    result = result.filter(item => this.ms.pinnedGeoMarker.getValue().indexOf(item.id) === -1);
                    this.currentResult = filteredResult.concat(result);
                },
                error => {
                    console.log('Error GeoRef');
                }
            );
        this.searchForm.valueChanges
            .pipe(
                filter(val => !this.mapStates.SEARCH_GEOREF),
                debounceTime(400),
                distinctUntilChanged(),
                switchMap((value: string) => {
                    return this._ss.searchIndices(value, null, null, null, null, this.ms.mapProperties.viewExtent || [-4, 42, 12, 24]);
                })
            )
            .subscribe(
                (result: GenericModel[]) => {
                    console.log('received ' + result.length);
                    this.ms.getFeaturesFromGeneric(result);
                },
                error => {
                    console.log('Error search geo');
                }
            );
        this.pinnedGeoMarkerSubs = this.ms.pinnedGeoMarker.subscribe((pinnedIds: string[]) => {
            this.pinnedIds = pinnedIds;
        });
    }

    ngAfterViewInit() {}

    ngOnDestroy() {
        if (this.pinnedGeoMarkerSubs) {
            this.pinnedGeoMarkerSubs.unsubscribe();
        }
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }
    }

    onEnter(event) {
        this.ms.getFeaturesFromGeoMarker(this.currentResult);
    }

    zoomToFeature(featId: string) {
        this.ms.zoomToFeature.next(new ZoomToFeatureRequest('geoMarkerLayer', featId));
    }

    pinnedFeature(feat: IMapDataDTO, addToMap?: boolean) {
        if (addToMap) {
            this.addSingleFeatureToMap(feat);
        }
        const currentValue = this.ms.pinnedGeoMarker.getValue();
        const i = currentValue.indexOf(feat.id);
        if (i !== -1) {
            currentValue.splice(i, 1);
        } else {
            currentValue.push(feat.id);
        }
        this.ms.pinnedGeoMarker.next(currentValue);
    }

    addSingleFeatureToMap(feat: IMapDataDTO) {
        this.ms.getFeaturesFromGeoMarker([feat]);
    }

    sendAction(action: string) {
        const currentState: MapState = this.ms.mapStates.getValue();
        switch (action) {
            case 'SEARCH_GEOREF':
                currentState.SEARCH_GEOREF = true;
                break;
            case 'SEARCH_INDICES':
                currentState.SEARCH_GEOREF = false;
                break;
            default:
                break;
        }
        this.ms.mapStates.next(currentState);
    }
}
