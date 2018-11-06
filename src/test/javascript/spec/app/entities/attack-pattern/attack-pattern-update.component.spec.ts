/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { AttackPatternUpdateComponent } from 'app/entities/attack-pattern/attack-pattern-update.component';
import { AttackPatternService } from 'app/entities/attack-pattern/attack-pattern.service';
import { AttackPattern } from 'app/shared/model/attack-pattern.model';

describe('Component Tests', () => {
    describe('AttackPattern Management Update Component', () => {
        let comp: AttackPatternUpdateComponent;
        let fixture: ComponentFixture<AttackPatternUpdateComponent>;
        let service: AttackPatternService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [AttackPatternUpdateComponent]
            })
                .overrideTemplate(AttackPatternUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AttackPatternUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttackPatternService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new AttackPattern(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.attackPattern = entity;
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
                    const entity = new AttackPattern();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.attackPattern = entity;
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
