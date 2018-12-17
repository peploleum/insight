/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { BiographicsDetailComponent } from 'app/entities/biographics/biographics-detail.component';
import { Biographics } from 'app/shared/model/biographics.model';

describe('Component Tests', () => {
    describe('Biographics Management Detail Component', () => {
        let comp: BiographicsDetailComponent;
        let fixture: ComponentFixture<BiographicsDetailComponent>;
        const route = ({ data: of({ biographics: new Biographics('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [BiographicsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BiographicsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BiographicsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.biographics).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
