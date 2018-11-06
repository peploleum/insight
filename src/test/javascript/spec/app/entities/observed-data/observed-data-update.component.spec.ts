/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ObservedDataUpdateComponent } from 'app/entities/observed-data/observed-data-update.component';
import { ObservedDataService } from 'app/entities/observed-data/observed-data.service';
import { ObservedData } from 'app/shared/model/observed-data.model';

describe('Component Tests', () => {
    describe('ObservedData Management Update Component', () => {
        let comp: ObservedDataUpdateComponent;
        let fixture: ComponentFixture<ObservedDataUpdateComponent>;
        let service: ObservedDataService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ObservedDataUpdateComponent]
            })
                .overrideTemplate(ObservedDataUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ObservedDataUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ObservedDataService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ObservedData(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.observedData = entity;
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
                    const entity = new ObservedData();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.observedData = entity;
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
