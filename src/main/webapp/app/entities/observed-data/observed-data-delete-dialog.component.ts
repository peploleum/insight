import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IObservedData } from 'app/shared/model/observed-data.model';
import { ObservedDataService } from './observed-data.service';

@Component({
    selector: 'jhi-observed-data-delete-dialog',
    templateUrl: './observed-data-delete-dialog.component.html'
})
export class ObservedDataDeleteDialogComponent {
    observedData: IObservedData;

    constructor(
        private observedDataService: ObservedDataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.observedDataService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'observedDataListModification',
                content: 'Deleted an observedData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-observed-data-delete-popup',
    template: ''
})
export class ObservedDataDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ observedData }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ObservedDataDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.observedData = observedData;
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
