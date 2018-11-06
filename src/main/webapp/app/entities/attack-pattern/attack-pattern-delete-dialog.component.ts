import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAttackPattern } from 'app/shared/model/attack-pattern.model';
import { AttackPatternService } from './attack-pattern.service';

@Component({
    selector: 'jhi-attack-pattern-delete-dialog',
    templateUrl: './attack-pattern-delete-dialog.component.html'
})
export class AttackPatternDeleteDialogComponent {
    attackPattern: IAttackPattern;

    constructor(
        private attackPatternService: AttackPatternService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.attackPatternService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'attackPatternListModification',
                content: 'Deleted an attackPattern'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-attack-pattern-delete-popup',
    template: ''
})
export class AttackPatternDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ attackPattern }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AttackPatternDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.attackPattern = attackPattern;
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
