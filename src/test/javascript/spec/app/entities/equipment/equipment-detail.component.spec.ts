/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { EquipmentDetailComponent } from 'app/entities/equipment/equipment-detail.component';
import { Equipment } from 'app/shared/model/equipment.model';

describe('Component Tests', () => {
    describe('Equipment Management Detail Component', () => {
        let comp: EquipmentDetailComponent;
        let fixture: ComponentFixture<EquipmentDetailComponent>;
        const route = ({ data: of({ equipment: new Equipment('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [EquipmentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EquipmentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EquipmentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.equipment).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
