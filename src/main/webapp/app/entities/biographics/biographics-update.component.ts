import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from './biographics.service';

@Component({
    selector: 'ins-biographics-update',
    templateUrl: './biographics-update.component.html'
})
export class BiographicsUpdateComponent implements OnInit {
    biographics: IBiographics;
    isSaving: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected biographicsService: BiographicsService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ biographics }) => {
            this.biographics = biographics;
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

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.biographics, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.biographics.id !== undefined) {
            this.subscribeToSaveResponse(this.biographicsService.update(this.biographics));
        } else {
            this.subscribeToSaveResponse(this.biographicsService.create(this.biographics));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBiographics>>) {
        result.subscribe((res: HttpResponse<IBiographics>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
