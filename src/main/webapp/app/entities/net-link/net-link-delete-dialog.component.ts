import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INetLink } from 'app/shared/model/net-link.model';
import { NetLinkService } from './net-link.service';

@Component({
    selector: 'jhi-net-link-delete-dialog',
    templateUrl: './net-link-delete-dialog.component.html'
})
export class NetLinkDeleteDialogComponent {
    netLink: INetLink;

    constructor(private netLinkService: NetLinkService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.netLinkService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'netLinkListModification',
                content: 'Deleted an netLink'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-net-link-delete-popup',
    template: ''
})
export class NetLinkDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ netLink }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(NetLinkDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.netLink = netLink;
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
