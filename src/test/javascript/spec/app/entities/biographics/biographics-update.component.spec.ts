/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { BiographicsUpdateComponent } from 'app/entities/biographics/biographics-update.component';
import { BiographicsService } from 'app/entities/biographics/biographics.service';
import { Biographics } from 'app/shared/model/biographics.model';

describe('Component Tests', () => {
    describe('Biographics Management Update Component', () => {
        let comp: BiographicsUpdateComponent;
        let fixture: ComponentFixture<BiographicsUpdateComponent>;
        let service: BiographicsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [BiographicsUpdateComponent]
            })
                .overrideTemplate(BiographicsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BiographicsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BiographicsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Biographics('123');
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.biographics = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Biographics();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.biographics = entity;
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
