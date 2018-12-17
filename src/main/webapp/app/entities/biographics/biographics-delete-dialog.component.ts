import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from './biographics.service';

@Component({
    selector: 'ins-biographics-delete-dialog',
    templateUrl: './biographics-delete-dialog.component.html'
})
export class BiographicsDeleteDialogComponent {
    biographics: IBiographics;

    constructor(
        protected biographicsService: BiographicsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.biographicsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'biographicsListModification',
                content: 'Deleted an biographics'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'ins-biographics-delete-popup',
    template: ''
})
export class BiographicsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ biographics }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BiographicsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.biographics = biographics;
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
