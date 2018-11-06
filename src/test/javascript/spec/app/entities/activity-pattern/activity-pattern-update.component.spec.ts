/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ActivityPatternUpdateComponent } from 'app/entities/activity-pattern/activity-pattern-update.component';
import { ActivityPatternService } from 'app/entities/activity-pattern/activity-pattern.service';
import { ActivityPattern } from 'app/shared/model/activity-pattern.model';

describe('Component Tests', () => {
    describe('ActivityPattern Management Update Component', () => {
        let comp: ActivityPatternUpdateComponent;
        let fixture: ComponentFixture<ActivityPatternUpdateComponent>;
        let service: ActivityPatternService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ActivityPatternUpdateComponent]
            })
                .overrideTemplate(ActivityPatternUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ActivityPatternUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ActivityPatternService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ActivityPattern(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.activityPattern = entity;
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
                    const entity = new ActivityPattern();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.activityPattern = entity;
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
