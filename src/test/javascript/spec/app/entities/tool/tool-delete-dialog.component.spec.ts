/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { InsightTestModule } from '../../../test.module';
import { ToolDeleteDialogComponent } from 'app/entities/tool/tool-delete-dialog.component';
import { ToolService } from 'app/entities/tool/tool.service';

describe('Component Tests', () => {
    describe('Tool Management Delete Component', () => {
        let comp: ToolDeleteDialogComponent;
        let fixture: ComponentFixture<ToolDeleteDialogComponent>;
        let service: ToolService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ToolDeleteDialogComponent]
            })
                .overrideTemplate(ToolDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ToolDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ToolService);
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
