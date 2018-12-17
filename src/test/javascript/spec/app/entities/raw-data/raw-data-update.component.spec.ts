/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { RawDataUpdateComponent } from 'app/entities/raw-data/raw-data-update.component';
import { RawDataService } from 'app/entities/raw-data/raw-data.service';
import { RawData } from 'app/shared/model/raw-data.model';

describe('Component Tests', () => {
    describe('RawData Management Update Component', () => {
        let comp: RawDataUpdateComponent;
        let fixture: ComponentFixture<RawDataUpdateComponent>;
        let service: RawDataService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [RawDataUpdateComponent]
            })
                .overrideTemplate(RawDataUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RawDataUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RawDataService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RawData('123');
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rawData = entity;
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
                    const entity = new RawData();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rawData = entity;
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
