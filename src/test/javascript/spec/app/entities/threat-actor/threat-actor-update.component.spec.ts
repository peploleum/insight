/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ThreatActorUpdateComponent } from 'app/entities/threat-actor/threat-actor-update.component';
import { ThreatActorService } from 'app/entities/threat-actor/threat-actor.service';
import { ThreatActor } from 'app/shared/model/threat-actor.model';

describe('Component Tests', () => {
    describe('ThreatActor Management Update Component', () => {
        let comp: ThreatActorUpdateComponent;
        let fixture: ComponentFixture<ThreatActorUpdateComponent>;
        let service: ThreatActorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ThreatActorUpdateComponent]
            })
                .overrideTemplate(ThreatActorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ThreatActorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ThreatActorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ThreatActor(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.threatActor = entity;
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
                    const entity = new ThreatActor();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.threatActor = entity;
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
