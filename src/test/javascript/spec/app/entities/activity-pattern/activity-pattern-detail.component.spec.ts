/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ActivityPatternDetailComponent } from 'app/entities/activity-pattern/activity-pattern-detail.component';
import { ActivityPattern } from 'app/shared/model/activity-pattern.model';

describe('Component Tests', () => {
    describe('ActivityPattern Management Detail Component', () => {
        let comp: ActivityPatternDetailComponent;
        let fixture: ComponentFixture<ActivityPatternDetailComponent>;
        const route = ({ data: of({ activityPattern: new ActivityPattern(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ActivityPatternDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ActivityPatternDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ActivityPatternDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.activityPattern).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
