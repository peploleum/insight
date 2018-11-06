import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';
import { IntrusionSetService } from './intrusion-set.service';
import { IActor } from 'app/shared/model/actor.model';
import { ActorService } from 'app/entities/actor';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-intrusion-set-update',
    templateUrl: './intrusion-set-update.component.html'
})
export class IntrusionSetUpdateComponent implements OnInit {
    private _intrusionSet: IIntrusionSet;
    isSaving: boolean;

    actors: IActor[];

    netlinks: INetLink[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private intrusionSetService: IntrusionSetService,
        private actorService: ActorService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ intrusionSet }) => {
            this.intrusionSet = intrusionSet;
        });
        this.actorService.query().subscribe(
            (res: HttpResponse<IActor[]>) => {
                this.actors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        if (this.intrusionSet.id !== undefined) {
            this.subscribeToSaveResponse(this.intrusionSetService.update(this.intrusionSet));
        } else {
            this.subscribeToSaveResponse(this.intrusionSetService.create(this.intrusionSet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIntrusionSet>>) {
        result.subscribe((res: HttpResponse<IIntrusionSet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackActorById(index: number, item: IActor) {
        return item.id;
    }

    trackNetLinkById(index: number, item: INetLink) {
        return item.id;
    }
    get intrusionSet() {
        return this._intrusionSet;
    }

    set intrusionSet(intrusionSet: IIntrusionSet) {
        this._intrusionSet = intrusionSet;
    }
}
