import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAttackPattern } from 'app/shared/model/attack-pattern.model';
import { AttackPatternService } from './attack-pattern.service';
import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from 'app/entities/net-link';

@Component({
    selector: 'jhi-attack-pattern-update',
    templateUrl: './attack-pattern-update.component.html'
})
export class AttackPatternUpdateComponent implements OnInit {
    private _attackPattern: IAttackPattern;
    isSaving: boolean;

    netlinks: INetLink[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private attackPatternService: AttackPatternService,
        private netLinkService: NetLinkService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ attackPattern }) => {
            this.attackPattern = attackPattern;
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
        if (this.attackPattern.id !== undefined) {
            this.subscribeToSaveResponse(this.attackPatternService.update(this.attackPattern));
        } else {
            this.subscribeToSaveResponse(this.attackPatternService.create(this.attackPattern));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAttackPattern>>) {
        result.subscribe((res: HttpResponse<IAttackPattern>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get attackPattern() {
        return this._attackPattern;
    }

    set attackPattern(attackPattern: IAttackPattern) {
        this._attackPattern = attackPattern;
    }
}
