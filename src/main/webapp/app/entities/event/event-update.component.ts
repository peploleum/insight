import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IEvent } from 'app/shared/model/event.model';
import { EventService } from './event.service';
import { IEquipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from 'app/entities/equipment';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IOrganisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from 'app/entities/organisation';
import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from 'app/entities/biographics';

@Component({
    selector: 'jhi-event-update',
    templateUrl: './event-update.component.html'
})
export class EventUpdateComponent implements OnInit {
    private _event: IEvent;
    isSaving: boolean;

    equipment: IEquipment[];

    locations: ILocation[];

    organisations: IOrganisation[];

    biographics: IBiographics[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private eventService: EventService,
        private equipmentService: EquipmentService,
        private locationService: LocationService,
        private organisationService: OrganisationService,
        private biographicsService: BiographicsService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ event }) => {
            this.event = event;
        });
        this.equipmentService.query().subscribe(
            (res: HttpResponse<IEquipment[]>) => {
                this.equipment = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.locationService.query().subscribe(
            (res: HttpResponse<ILocation[]>) => {
                this.locations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organisationService.query().subscribe(
            (res: HttpResponse<IOrganisation[]>) => {
                this.organisations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.biographicsService.query().subscribe(
            (res: HttpResponse<IBiographics[]>) => {
                this.biographics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.event.id !== undefined) {
            this.subscribeToSaveResponse(this.eventService.update(this.event));
        } else {
            this.subscribeToSaveResponse(this.eventService.create(this.event));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>) {
        result.subscribe((res: HttpResponse<IEvent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEquipmentById(index: number, item: IEquipment) {
        return item.id;
    }

    trackLocationById(index: number, item: ILocation) {
        return item.id;
    }

    trackOrganisationById(index: number, item: IOrganisation) {
        return item.id;
    }

    trackBiographicsById(index: number, item: IBiographics) {
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
    get event() {
        return this._event;
    }

    set event(event: IEvent) {
        this._event = event;
    }
}
