import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IOrganisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from './organisation.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from 'app/entities/biographics';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event';
import { IEquipment } from 'app/shared/model/equipment.model';
import { EquipmentService } from 'app/entities/equipment';

@Component({
    selector: 'jhi-organisation-update',
    templateUrl: './organisation-update.component.html'
})
export class OrganisationUpdateComponent implements OnInit {
    private _organisation: IOrganisation;
    isSaving: boolean;

    locations: ILocation[];

    biographics: IBiographics[];

    events: IEvent[];

    equipment: IEquipment[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private organisationService: OrganisationService,
        private locationService: LocationService,
        private biographicsService: BiographicsService,
        private eventService: EventService,
        private equipmentService: EquipmentService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ organisation }) => {
            this.organisation = organisation;
        });
        this.locationService.query().subscribe(
            (res: HttpResponse<ILocation[]>) => {
                this.locations = res.body;
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
        this.equipmentService.query().subscribe(
            (res: HttpResponse<IEquipment[]>) => {
                this.equipment = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.organisation.id !== undefined) {
            this.subscribeToSaveResponse(this.organisationService.update(this.organisation));
        } else {
            this.subscribeToSaveResponse(this.organisationService.create(this.organisation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisation>>) {
        result.subscribe((res: HttpResponse<IOrganisation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBiographicsById(index: number, item: IBiographics) {
        return item.id;
    }

    trackEventById(index: number, item: IEvent) {
        return item.id;
    }

    trackEquipmentById(index: number, item: IEquipment) {
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
    get organisation() {
        return this._organisation;
    }

    set organisation(organisation: IOrganisation) {
        this._organisation = organisation;
    }
}
