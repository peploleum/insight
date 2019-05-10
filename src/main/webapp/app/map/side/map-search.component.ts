/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, OnChanges, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { SideInterface } from '../../side/side.abstract';
import { MapService } from '../map.service';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs/internal/operators';
import { IMapDataDTO } from '../../shared/model/map.model';
import { MapState, ZoomToFeatureRequest } from '../../shared/util/map-utils';
import { Subscription } from 'rxjs/index';
import { SearchService } from '../../shared/search/search.service';
import { GenericModel } from '../../shared/model/generic.model';
import { SideMediatorService } from '../../side/side-mediator.service';
import { ENTITY_TYPE_LIST, getGenericNameProperty, getGenericSymbolProperty, toKebabCase } from '../../shared/util/insight-util';
import { InsightSearchComponent } from '../../shared/search/insight-search.component';
import { NetworkService } from '../../network/network.service';

@Component({
    selector: 'ins-map-search',
    templateUrl: './map-search.component.html'
})
export class MapSearchComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    entityQueryResult: GenericModel[] = [];
    selectedEntityTypes: string[] = ENTITY_TYPE_LIST;
    @ViewChildren(InsightSearchComponent) entitySearchForm: QueryList<InsightSearchComponent>;

    geoRefQueryResult: IMapDataDTO[] = [];
    geoRefSearchForm: FormControl = new FormControl('');
    pinnedIds: string[];

    mapStates: MapState;

    selected_data_ids: string[] = [];

    mapStatesSubs: Subscription;
    pinnedGeoMarkerSubs: Subscription;
    selectedDataSubs: Subscription;

    constructor(protected ms: MapService, private _ss: SearchService, private _sms: SideMediatorService) {
        super();
    }

    ngOnInit() {
        this.mapStatesSubs = this.ms.mapStates.subscribe(state => {
            this.mapStates = state;
        });
        this.geoRefSearchForm.valueChanges
            .pipe(
                debounceTime(400),
                distinctUntilChanged(),
                switchMap((value: string) => {
                    return this.ms.getGeoMarker(value);
                })
            )
            .subscribe(
                (result: IMapDataDTO[]) => {
                    // Garde les pinned points
                    const filteredResult = this.geoRefQueryResult.filter(
                        item => this.ms.pinnedGeoMarker.getValue().indexOf(item.id) !== -1
                    );
                    // Remove doublon
                    result = result.filter(item => this.ms.pinnedGeoMarker.getValue().indexOf(item.id) === -1);
                    this.geoRefQueryResult = filteredResult.concat(result);
                },
                error => {
                    console.log('Error GeoRef');
                }
            );

        this.pinnedGeoMarkerSubs = this.ms.pinnedGeoMarker.subscribe((pinnedIds: string[]) => {
            this.pinnedIds = pinnedIds;
        });

        this.selectedDataSubs = this._sms._selectedData.subscribe((ids: string[]) => {
            this.selected_data_ids = ids;
        });
    }

    ngAfterViewInit() {
        this.entitySearchForm.changes.subscribe(r => {
            if (r.first) {
                // Définie la fonction getExtent du searchComponent
                (<InsightSearchComponent>r.first).extentProvider = this.getExtent;
            }
        });
    }

    getExtent = (): any => {
        return this.ms.mapProperties.viewExtent;
    };

    ngOnDestroy() {
        if (this.pinnedGeoMarkerSubs) {
            this.pinnedGeoMarkerSubs.unsubscribe();
        }
        if (this.mapStatesSubs) {
            this.mapStatesSubs.unsubscribe();
        }

        if (this.selectedDataSubs) {
            this.selectedDataSubs.unsubscribe();
        }
    }

    onResultQueryReceived(result: GenericModel[]) {
        this.entityQueryResult = result;
        this.ms.getFeaturesFromGeneric(this.entityQueryResult, 'SEARCH');
    }

    onDataSelected(select: GenericModel) {
        this.entityQueryResult = [select];
        this.ms.getFeaturesFromGeneric(this.entityQueryResult, 'SEARCH');
    }

    onEnter(event) {
        this.ms.getFeaturesFromGeoMarker(this.geoRefQueryResult);
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

    modifyState(action: string) {
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

    sendAction(action: string) {
        if (action === 'CLEAR_SEARCH_FEATURE' && this.entitySearchForm.first) {
            (<InsightSearchComponent>this.entitySearchForm.first).clearSearchForm();
        }
        this._sms._onNewActionClicked.next(action);
    }

    getEntityNameProperty(entity: GenericModel): string {
        return getGenericNameProperty(entity);
    }

    getEntitySymbolProperty(entity: GenericModel): string {
        return getGenericSymbolProperty(entity);
    }

    getLink(str: string): string {
        return toKebabCase(str);
    }

    getDefaultEntitySymbol(entity: GenericModel): string {
        return NetworkService.getNodeImageUrl(entity['entityType']);
    }

    onEntityClick(id: string) {
        this._sms._onNewDataSelected.next([id]);
    }
}
