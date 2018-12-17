/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { EquipmentUpdateComponent } from 'app/entities/equipment/equipment-update.component';
import { EquipmentService } from 'app/entities/equipment/equipment.service';
import { Equipment } from 'app/shared/model/equipment.model';

describe('Component Tests', () => {
    describe('Equipment Management Update Component', () => {
        let comp: EquipmentUpdateComponent;
        let fixture: ComponentFixture<EquipmentUpdateComponent>;
        let service: EquipmentService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [EquipmentUpdateComponent]
            })
                .overrideTemplate(EquipmentUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EquipmentUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EquipmentService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Equipment('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.equipment = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Equipment();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.equipment = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
