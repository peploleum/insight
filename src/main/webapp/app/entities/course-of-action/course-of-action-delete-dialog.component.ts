import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseOfAction } from 'app/shared/model/course-of-action.model';
import { CourseOfActionService } from './course-of-action.service';

@Component({
    selector: 'jhi-course-of-action-delete-dialog',
    templateUrl: './course-of-action-delete-dialog.component.html'
})
export class CourseOfActionDeleteDialogComponent {
    courseOfAction: ICourseOfAction;

    constructor(
        private courseOfActionService: CourseOfActionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courseOfActionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'courseOfActionListModification',
                content: 'Deleted an courseOfAction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-course-of-action-delete-popup',
    template: ''
})
export class CourseOfActionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ courseOfAction }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CourseOfActionDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.courseOfAction = courseOfAction;
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
