/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { NetLinkDetailComponent } from 'app/entities/net-link/net-link-detail.component';
import { NetLink } from 'app/shared/model/net-link.model';

describe('Component Tests', () => {
    describe('NetLink Management Detail Component', () => {
        let comp: NetLinkDetailComponent;
        let fixture: ComponentFixture<NetLinkDetailComponent>;
        const route = ({ data: of({ netLink: new NetLink(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [NetLinkDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(NetLinkDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(NetLinkDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.netLink).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
