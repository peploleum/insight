/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { ObservedDataDeleteDialogComponent } from 'app/entities/observed-data/observed-data-delete-dialog.component';
import { ObservedDataService } from 'app/entities/observed-data/observed-data.service';

describe('Component Tests', () => {
    describe('ObservedData Management Delete Component', () => {
        let comp: ObservedDataDeleteDialogComponent;
        let fixture: ComponentFixture<ObservedDataDeleteDialogComponent>;
        let service: ObservedDataService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ObservedDataDeleteDialogComponent]
            })
                .overrideTemplate(ObservedDataDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ObservedDataDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ObservedDataService);
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
