import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IThreatActor } from 'app/shared/model/threat-actor.model';
import { ThreatActorService } from './threat-actor.service';

@Component({
    selector: 'jhi-threat-actor-delete-dialog',
    templateUrl: './threat-actor-delete-dialog.component.html'
})
export class ThreatActorDeleteDialogComponent {
    threatActor: IThreatActor;

    constructor(
        private threatActorService: ThreatActorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.threatActorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'threatActorListModification',
                content: 'Deleted an threatActor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-threat-actor-delete-popup',
    template: ''
})
export class ThreatActorDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ threatActor }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ThreatActorDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.threatActor = threatActor;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
