/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { IntrusionSetDeleteDialogComponent } from 'app/entities/intrusion-set/intrusion-set-delete-dialog.component';
import { IntrusionSetService } from 'app/entities/intrusion-set/intrusion-set.service';

describe('Component Tests', () => {
    describe('IntrusionSet Management Delete Component', () => {
        let comp: IntrusionSetDeleteDialogComponent;
        let fixture: ComponentFixture<IntrusionSetDeleteDialogComponent>;
        let service: IntrusionSetService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [IntrusionSetDeleteDialogComponent]
            })
                .overrideTemplate(IntrusionSetDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IntrusionSetDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IntrusionSetService);
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
