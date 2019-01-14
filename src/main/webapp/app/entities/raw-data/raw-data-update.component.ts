import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { IRawData } from 'app/shared/model/raw-data.model';
import { RawDataService } from './raw-data.service';

@Component({
    selector: 'ins-raw-data-update',
    templateUrl: './raw-data-update.component.html'
})
export class RawDataUpdateComponent implements OnInit {
    rawData: IRawData;
    isSaving: boolean;
    rawDataCreationDate: string;
    rawDataExtractedDate: string;

    constructor(protected dataUtils: JhiDataUtils, protected rawDataService: RawDataService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ rawData }) => {
            this.rawData = rawData;
            this.rawDataCreationDate =
                this.rawData.rawDataCreationDate != null ? this.rawData.rawDataCreationDate.format(DATE_TIME_FORMAT) : null;
            this.rawDataExtractedDate =
                this.rawData.rawDataExtractedDate != null ? this.rawData.rawDataExtractedDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.rawData.rawDataCreationDate = this.rawDataCreationDate != null ? moment(this.rawDataCreationDate, DATE_TIME_FORMAT) : null;
        this.rawData.rawDataExtractedDate = this.rawDataExtractedDate != null ? moment(this.rawDataExtractedDate, DATE_TIME_FORMAT) : null;
        if (this.rawData.id !== undefined) {
            this.subscribeToSaveResponse(this.rawDataService.update(this.rawData));
        } else {
            this.subscribeToSaveResponse(this.rawDataService.create(this.rawData));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRawData>>) {
        result.subscribe((res: HttpResponse<IRawData>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
