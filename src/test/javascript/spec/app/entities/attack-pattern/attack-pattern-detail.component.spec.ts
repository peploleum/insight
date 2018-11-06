/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { AttackPatternDetailComponent } from 'app/entities/attack-pattern/attack-pattern-detail.component';
import { AttackPattern } from 'app/shared/model/attack-pattern.model';

describe('Component Tests', () => {
    describe('AttackPattern Management Detail Component', () => {
        let comp: AttackPatternDetailComponent;
        let fixture: ComponentFixture<AttackPatternDetailComponent>;
        const route = ({ data: of({ attackPattern: new AttackPattern(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [AttackPatternDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AttackPatternDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AttackPatternDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.attackPattern).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
