/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { AttackPatternDeleteDialogComponent } from 'app/entities/attack-pattern/attack-pattern-delete-dialog.component';
import { AttackPatternService } from 'app/entities/attack-pattern/attack-pattern.service';

describe('Component Tests', () => {
    describe('AttackPattern Management Delete Component', () => {
        let comp: AttackPatternDeleteDialogComponent;
        let fixture: ComponentFixture<AttackPatternDeleteDialogComponent>;
        let service: AttackPatternService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [AttackPatternDeleteDialogComponent]
            })
                .overrideTemplate(AttackPatternDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttackPatternDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttackPatternService);
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
