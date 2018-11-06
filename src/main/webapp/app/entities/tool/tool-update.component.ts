import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITool } from 'app/shared/model/tool.model';
import { ToolService } from './tool.service';
import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';
import { IntrusionSetService } from 'app/entities/intrusion-set';
import { IMalware } from 'app/shared/model/malware.model';
import { MalwareService } from 'app/entities/malware';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';
import { IThreatActor } from 'app/shared/model/threat-actor.model';
import { ThreatActorService } from 'app/entities/threat-actor';

@Component({
    selector: 'jhi-tool-update',
    templateUrl: './tool-update.component.html'
})
export class ToolUpdateComponent implements OnInit {
    private _tool: ITool;
    isSaving: boolean;

    intrusionsets: IIntrusionSet[];

    malwares: IMalware[];

    netlinks: INetLink[];

    threatactors: IThreatActor[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private toolService: ToolService,
        private intrusionSetService: IntrusionSetService,
        private malwareService: MalwareService,
        private netLinkService: NetLinkService,
        private threatActorService: ThreatActorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ tool }) => {
            this.tool = tool;
        });
        this.intrusionSetService.query().subscribe(
            (res: HttpResponse<IIntrusionSet[]>) => {
                this.intrusionsets = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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
        this.threatActorService.query().subscribe(
            (res: HttpResponse<IThreatActor[]>) => {
                this.threatactors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.tool.id !== undefined) {
            this.subscribeToSaveResponse(this.toolService.update(this.tool));
        } else {
            this.subscribeToSaveResponse(this.toolService.create(this.tool));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITool>>) {
        result.subscribe((res: HttpResponse<ITool>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackIntrusionSetById(index: number, item: IIntrusionSet) {
        return item.id;
    }

    trackMalwareById(index: number, item: IMalware) {
        return item.id;
    }

    trackNetLinkById(index: number, item: INetLink) {
        return item.id;
    }

    trackThreatActorById(index: number, item: IThreatActor) {
        return item.id;
    }
    get tool() {
        return this._tool;
    }

    set tool(tool: ITool) {
        this._tool = tool;
    }
}
