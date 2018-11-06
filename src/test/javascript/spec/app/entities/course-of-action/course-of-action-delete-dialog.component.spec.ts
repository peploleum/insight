/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { CourseOfActionDeleteDialogComponent } from 'app/entities/course-of-action/course-of-action-delete-dialog.component';
import { CourseOfActionService } from 'app/entities/course-of-action/course-of-action.service';

describe('Component Tests', () => {
    describe('CourseOfAction Management Delete Component', () => {
        let comp: CourseOfActionDeleteDialogComponent;
        let fixture: ComponentFixture<CourseOfActionDeleteDialogComponent>;
        let service: CourseOfActionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [CourseOfActionDeleteDialogComponent]
            })
                .overrideTemplate(CourseOfActionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CourseOfActionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseOfActionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
