/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { ActivityPatternDeleteDialogComponent } from 'app/entities/activity-pattern/activity-pattern-delete-dialog.component';
import { ActivityPatternService } from 'app/entities/activity-pattern/activity-pattern.service';

describe('Component Tests', () => {
    describe('ActivityPattern Management Delete Component', () => {
        let comp: ActivityPatternDeleteDialogComponent;
        let fixture: ComponentFixture<ActivityPatternDeleteDialogComponent>;
        let service: ActivityPatternService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ActivityPatternDeleteDialogComponent]
            })
                .overrideTemplate(ActivityPatternDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ActivityPatternDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActivityPatternService);
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
