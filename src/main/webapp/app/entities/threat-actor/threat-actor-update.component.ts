import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IThreatActor } from 'app/shared/model/threat-actor.model';
import { ThreatActorService } from './threat-actor.service';
import { IMalware } from 'app/shared/model/malware.model';
import { MalwareService } from 'app/entities/malware';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-threat-actor-update',
    templateUrl: './threat-actor-update.component.html'
})
export class ThreatActorUpdateComponent implements OnInit {
    private _threatActor: IThreatActor;
    isSaving: boolean;

    malwares: IMalware[];

    netlinks: INetLink[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private threatActorService: ThreatActorService,
        private malwareService: MalwareService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ threatActor }) => {
            this.threatActor = threatActor;
        });
        this.malwareService.query().subscribe(
            (res: HttpResponse<IMalware[]>) => {
                this.malwares = res.body;
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
        if (this.threatActor.id !== undefined) {
            this.subscribeToSaveResponse(this.threatActorService.update(this.threatActor));
        } else {
            this.subscribeToSaveResponse(this.threatActorService.create(this.threatActor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IThreatActor>>) {
        result.subscribe((res: HttpResponse<IThreatActor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMalwareById(index: number, item: IMalware) {
        return item.id;
    }

    trackNetLinkById(index: number, item: INetLink) {
        return item.id;
    }
    get threatActor() {
        return this._threatActor;
    }

    set threatActor(threatActor: IThreatActor) {
        this._threatActor = threatActor;
    }
}
