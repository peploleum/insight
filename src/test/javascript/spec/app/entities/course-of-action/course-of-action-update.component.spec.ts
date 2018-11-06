/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { CourseOfActionUpdateComponent } from 'app/entities/course-of-action/course-of-action-update.component';
import { CourseOfActionService } from 'app/entities/course-of-action/course-of-action.service';
import { CourseOfAction } from 'app/shared/model/course-of-action.model';

describe('Component Tests', () => {
    describe('CourseOfAction Management Update Component', () => {
        let comp: CourseOfActionUpdateComponent;
        let fixture: ComponentFixture<CourseOfActionUpdateComponent>;
        let service: CourseOfActionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [CourseOfActionUpdateComponent]
            })
                .overrideTemplate(CourseOfActionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CourseOfActionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseOfActionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CourseOfAction(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.courseOfAction = entity;
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
                    const entity = new CourseOfAction();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.courseOfAction = entity;
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
