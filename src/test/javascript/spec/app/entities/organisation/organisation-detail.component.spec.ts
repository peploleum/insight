/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { OrganisationDetailComponent } from 'app/entities/organisation/organisation-detail.component';
import { Organisation } from 'app/shared/model/organisation.model';

describe('Component Tests', () => {
    describe('Organisation Management Detail Component', () => {
        let comp: OrganisationDetailComponent;
        let fixture: ComponentFixture<OrganisationDetailComponent>;
        const route = ({ data: of({ organisation: new Organisation('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [OrganisationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(OrganisationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(OrganisationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.organisation).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
