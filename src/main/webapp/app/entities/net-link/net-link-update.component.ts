import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from './net-link.service';

@Component({
    selector: 'jhi-net-link-update',
    templateUrl: './net-link-update.component.html'
})
export class NetLinkUpdateComponent implements OnInit {
    private _netLink: INetLink;
    isSaving: boolean;

    constructor(private netLinkService: NetLinkService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ netLink }) => {
            this.netLink = netLink;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.netLink.id !== undefined) {
            this.subscribeToSaveResponse(this.netLinkService.update(this.netLink));
        } else {
            this.subscribeToSaveResponse(this.netLinkService.create(this.netLink));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<INetLink>>) {
        result.subscribe((res: HttpResponse<INetLink>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get netLink() {
        return this._netLink;
    }

    set netLink(netLink: INetLink) {
        this._netLink = netLink;
    }
}
