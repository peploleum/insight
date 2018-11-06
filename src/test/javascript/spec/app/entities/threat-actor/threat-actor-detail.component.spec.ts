/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ThreatActorDetailComponent } from 'app/entities/threat-actor/threat-actor-detail.component';
import { ThreatActor } from 'app/shared/model/threat-actor.model';

describe('Component Tests', () => {
    describe('ThreatActor Management Detail Component', () => {
        let comp: ThreatActorDetailComponent;
        let fixture: ComponentFixture<ThreatActorDetailComponent>;
        const route = ({ data: of({ threatActor: new ThreatActor(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ThreatActorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ThreatActorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ThreatActorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.threatActor).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
