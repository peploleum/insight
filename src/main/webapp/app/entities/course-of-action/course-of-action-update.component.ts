import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICourseOfAction } from 'app/shared/model/course-of-action.model';
import { CourseOfActionService } from './course-of-action.service';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-course-of-action-update',
    templateUrl: './course-of-action-update.component.html'
})
export class CourseOfActionUpdateComponent implements OnInit {
    private _courseOfAction: ICourseOfAction;
    isSaving: boolean;

    netlinks: INetLink[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private courseOfActionService: CourseOfActionService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ courseOfAction }) => {
            this.courseOfAction = courseOfAction;
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
        if (this.courseOfAction.id !== undefined) {
            this.subscribeToSaveResponse(this.courseOfActionService.update(this.courseOfAction));
        } else {
            this.subscribeToSaveResponse(this.courseOfActionService.create(this.courseOfAction));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICourseOfAction>>) {
        result.subscribe((res: HttpResponse<ICourseOfAction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get courseOfAction() {
        return this._courseOfAction;
    }

    set courseOfAction(courseOfAction: ICourseOfAction) {
        this._courseOfAction = courseOfAction;
    }
}
