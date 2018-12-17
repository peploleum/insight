import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRawData } from 'app/shared/model/raw-data.model';
import { RawDataService } from './raw-data.service';

@Component({
    selector: 'ins-raw-data-delete-dialog',
    templateUrl: './raw-data-delete-dialog.component.html'
})
export class RawDataDeleteDialogComponent {
    rawData: IRawData;

    constructor(protected rawDataService: RawDataService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: string) {
        this.rawDataService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rawDataListModification',
                content: 'Deleted an rawData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'ins-raw-data-delete-popup',
    template: ''
})
export class RawDataDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rawData }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RawDataDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rawData = rawData;
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
