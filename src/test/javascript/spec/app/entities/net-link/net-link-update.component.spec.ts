/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { NetLinkUpdateComponent } from 'app/entities/net-link/net-link-update.component';
import { NetLinkService } from 'app/entities/net-link/net-link.service';
import { NetLink } from 'app/shared/model/net-link.model';

describe('Component Tests', () => {
    describe('NetLink Management Update Component', () => {
        let comp: NetLinkUpdateComponent;
        let fixture: ComponentFixture<NetLinkUpdateComponent>;
        let service: NetLinkService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [NetLinkUpdateComponent]
            })
                .overrideTemplate(NetLinkUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NetLinkUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NetLinkService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new NetLink(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.netLink = entity;
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
                    const entity = new NetLink();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.netLink = entity;
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
