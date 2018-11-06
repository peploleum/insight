import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IActivityPattern } from 'app/shared/model/activity-pattern.model';
import { ActivityPatternService } from './activity-pattern.service';

@Component({
    selector: 'jhi-activity-pattern-delete-dialog',
    templateUrl: './activity-pattern-delete-dialog.component.html'
})
export class ActivityPatternDeleteDialogComponent {
    activityPattern: IActivityPattern;

    constructor(
        private activityPatternService: ActivityPatternService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.activityPatternService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'activityPatternListModification',
                content: 'Deleted an activityPattern'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-activity-pattern-delete-popup',
    template: ''
})
export class ActivityPatternDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ activityPattern }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ActivityPatternDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.activityPattern = activityPattern;
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
