import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IObservedData } from 'app/shared/model/observed-data.model';
import { ObservedDataService } from './observed-data.service';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-observed-data-update',
    templateUrl: './observed-data-update.component.html'
})
export class ObservedDataUpdateComponent implements OnInit {
    private _observedData: IObservedData;
    isSaving: boolean;

    netlinks: INetLink[];
    dateDebut: string;
    dateFin: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private observedDataService: ObservedDataService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ observedData }) => {
            this.observedData = observedData;
        });
        this.netLinkService.query().subscribe(
            (res: HttpResponse<INetLink[]>) => {
                this.netlinks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.observedData.dateDebut = moment(this.dateDebut, DATE_TIME_FORMAT);
        this.observedData.dateFin = moment(this.dateFin, DATE_TIME_FORMAT);
        if (this.observedData.id !== undefined) {
            this.subscribeToSaveResponse(this.observedDataService.update(this.observedData));
        } else {
            this.subscribeToSaveResponse(this.observedDataService.create(this.observedData));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IObservedData>>) {
        result.subscribe((res: HttpResponse<IObservedData>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackNetLinkById(index: number, item: INetLink) {
        return item.id;
    }
    get observedData() {
        return this._observedData;
    }

    set observedData(observedData: IObservedData) {
        this._observedData = observedData;
        this.dateDebut = moment(observedData.dateDebut).format(DATE_TIME_FORMAT);
        this.dateFin = moment(observedData.dateFin).format(DATE_TIME_FORMAT);
    }
}
