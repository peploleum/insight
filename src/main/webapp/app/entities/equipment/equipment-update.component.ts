import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IEquipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from './equipment.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IOrganisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from 'app/entities/organisation';
import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from 'app/entities/biographics';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event';

@Component({
    selector: 'jhi-equipment-update',
    templateUrl: './equipment-update.component.html'
})
export class EquipmentUpdateComponent implements OnInit {
    private _equipment: IEquipment;
    isSaving: boolean;

    locations: ILocation[];

    organisations: IOrganisation[];

    biographics: IBiographics[];

    events: IEvent[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private equipmentService: EquipmentService,
        private locationService: LocationService,
        private organisationService: OrganisationService,
        private biographicsService: BiographicsService,
        private eventService: EventService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ equipment }) => {
            this.equipment = equipment;
        });
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
        this.eventService.query().subscribe(
            (res: HttpResponse<IEvent[]>) => {
                this.events = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.equipment.id !== undefined) {
            this.subscribeToSaveResponse(this.equipmentService.update(this.equipment));
        } else {
            this.subscribeToSaveResponse(this.equipmentService.create(this.equipment));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEquipment>>) {
        result.subscribe((res: HttpResponse<IEquipment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackLocationById(index: number, item: ILocation) {
        return item.id;
    }

    trackOrganisationById(index: number, item: IOrganisation) {
        return item.id;
    }

    trackBiographicsById(index: number, item: IBiographics) {
        return item.id;
    }

    trackEventById(index: number, item: IEvent) {
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
    get equipment() {
        return this._equipment;
    }

    set equipment(equipment: IEquipment) {
        this._equipment = equipment;
    }
}
