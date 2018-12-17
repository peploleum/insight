import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IOrganisation } from 'app/shared/model/organisation.model';
import { OrganisationService } from './organisation.service';

@Component({
    selector: 'ins-organisation-update',
    templateUrl: './organisation-update.component.html'
})
export class OrganisationUpdateComponent implements OnInit {
    organisation: IOrganisation;
    isSaving: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected organisationService: OrganisationService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ organisation }) => {
            this.organisation = organisation;
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
        this.dataUtils.clearInputImage(this.organisation, this.elementRef, field, fieldContentType, idInput);
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

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganisation>>) {
        result.subscribe((res: HttpResponse<IOrganisation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
