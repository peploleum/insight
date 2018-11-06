import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IActivityPattern } from 'app/shared/model/activity-pattern.model';
import { ActivityPatternService } from './activity-pattern.service';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-activity-pattern-update',
    templateUrl: './activity-pattern-update.component.html'
})
export class ActivityPatternUpdateComponent implements OnInit {
    private _activityPattern: IActivityPattern;
    isSaving: boolean;

    netlinks: INetLink[];
    valideAPartirDe: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private activityPatternService: ActivityPatternService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ activityPattern }) => {
            this.activityPattern = activityPattern;
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
        this.activityPattern.valideAPartirDe = moment(this.valideAPartirDe, DATE_TIME_FORMAT);
        if (this.activityPattern.id !== undefined) {
            this.subscribeToSaveResponse(this.activityPatternService.update(this.activityPattern));
        } else {
            this.subscribeToSaveResponse(this.activityPatternService.create(this.activityPattern));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IActivityPattern>>) {
        result.subscribe((res: HttpResponse<IActivityPattern>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get activityPattern() {
        return this._activityPattern;
    }

    set activityPattern(activityPattern: IActivityPattern) {
        this._activityPattern = activityPattern;
        this.valideAPartirDe = moment(activityPattern.valideAPartirDe).format(DATE_TIME_FORMAT);
    }
}
