/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { RawDataDeleteDialogComponent } from 'app/entities/raw-data/raw-data-delete-dialog.component';
import { RawDataService } from 'app/entities/raw-data/raw-data.service';

describe('Component Tests', () => {
    describe('RawData Management Delete Component', () => {
        let comp: RawDataDeleteDialogComponent;
        let fixture: ComponentFixture<RawDataDeleteDialogComponent>;
        let service: RawDataService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [RawDataDeleteDialogComponent]
            })
                .overrideTemplate(RawDataDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RawDataDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RawDataService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete('123');
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith('123');
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
