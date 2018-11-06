/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { CourseOfActionDetailComponent } from 'app/entities/course-of-action/course-of-action-detail.component';
import { CourseOfAction } from 'app/shared/model/course-of-action.model';

describe('Component Tests', () => {
    describe('CourseOfAction Management Detail Component', () => {
        let comp: CourseOfActionDetailComponent;
        let fixture: ComponentFixture<CourseOfActionDetailComponent>;
        const route = ({ data: of({ courseOfAction: new CourseOfAction(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [CourseOfActionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CourseOfActionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CourseOfActionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.courseOfAction).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
