/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { IntrusionSetUpdateComponent } from 'app/entities/intrusion-set/intrusion-set-update.component';
import { IntrusionSetService } from 'app/entities/intrusion-set/intrusion-set.service';
import { IntrusionSet } from 'app/shared/model/intrusion-set.model';

describe('Component Tests', () => {
    describe('IntrusionSet Management Update Component', () => {
        let comp: IntrusionSetUpdateComponent;
        let fixture: ComponentFixture<IntrusionSetUpdateComponent>;
        let service: IntrusionSetService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [IntrusionSetUpdateComponent]
            })
                .overrideTemplate(IntrusionSetUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(IntrusionSetUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IntrusionSetService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new IntrusionSet(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.intrusionSet = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new IntrusionSet();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.intrusionSet = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
