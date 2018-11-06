/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { ThreatActorDeleteDialogComponent } from 'app/entities/threat-actor/threat-actor-delete-dialog.component';
import { ThreatActorService } from 'app/entities/threat-actor/threat-actor.service';

describe('Component Tests', () => {
    describe('ThreatActor Management Delete Component', () => {
        let comp: ThreatActorDeleteDialogComponent;
        let fixture: ComponentFixture<ThreatActorDeleteDialogComponent>;
        let service: ThreatActorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ThreatActorDeleteDialogComponent]
            })
                .overrideTemplate(ThreatActorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ThreatActorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ThreatActorService);
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
