import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIntrusionSet } from 'app/shared/model/intrusion-set.model';
import { IntrusionSetService } from './intrusion-set.service';

@Component({
    selector: 'jhi-intrusion-set-delete-dialog',
    templateUrl: './intrusion-set-delete-dialog.component.html'
})
export class IntrusionSetDeleteDialogComponent {
    intrusionSet: IIntrusionSet;

    constructor(
        private intrusionSetService: IntrusionSetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.intrusionSetService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'intrusionSetListModification',
                content: 'Deleted an intrusionSet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-intrusion-set-delete-popup',
    template: ''
})
export class IntrusionSetDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ intrusionSet }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(IntrusionSetDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.intrusionSet = intrusionSet;
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
