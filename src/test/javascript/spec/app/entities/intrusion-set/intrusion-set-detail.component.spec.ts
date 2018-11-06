/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { IntrusionSetDetailComponent } from 'app/entities/intrusion-set/intrusion-set-detail.component';
import { IntrusionSet } from 'app/shared/model/intrusion-set.model';

describe('Component Tests', () => {
    describe('IntrusionSet Management Detail Component', () => {
        let comp: IntrusionSetDetailComponent;
        let fixture: ComponentFixture<IntrusionSetDetailComponent>;
        const route = ({ data: of({ intrusionSet: new IntrusionSet(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [IntrusionSetDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(IntrusionSetDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(IntrusionSetDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.intrusionSet).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
