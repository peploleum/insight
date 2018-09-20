import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from 'app/entities/biographics';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event';
import { IEquipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from 'app/entities/equipment';
import { IOrganisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from 'app/entities/organisation';

@Component({
    selector: 'jhi-location-update',
    templateUrl: './location-update.component.html'
})
export class LocationUpdateComponent implements OnInit {
    private _location: ILocation;
    isSaving: boolean;

    biographics: IBiographics[];

    events: IEvent[];

    equipment: IEquipment[];

    organisations: IOrganisation[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private locationService: LocationService,
        private biographicsService: BiographicsService,
        private eventService: EventService,
        private equipmentService: EquipmentService,
        private organisationService: OrganisationService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ location }) => {
            this.location = location;
        });
        this.biographicsService.query().subscribe(
            (res: HttpResponse<IBiographics[]>) => {
                this.biographics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.eventService.query().subscribe(
            (res: HttpResponse<IEvent[]>) => {
                this.events = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.equipmentService.query().subscribe(
            (res: HttpResponse<IEquipment[]>) => {
                this.equipment = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organisationService.query().subscribe(
            (res: HttpResponse<IOrganisation[]>) => {
                this.organisations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.location.id !== undefined) {
            this.subscribeToSaveResponse(this.locationService.update(this.location));
        } else {
            this.subscribeToSaveResponse(this.locationService.create(this.location));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>) {
        result.subscribe((res: HttpResponse<ILocation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBiographicsById(index: number, item: IBiographics) {
        return item.id;
    }

    trackEventById(index: number, item: IEvent) {
        return item.id;
    }

    trackEquipmentById(index: number, item: IEquipment) {
        return item.id;
    }

    trackOrganisationById(index: number, item: IOrganisation) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get location() {
        return this._location;
    }

    set location(location: ILocation) {
        this._location = location;
    }
}
