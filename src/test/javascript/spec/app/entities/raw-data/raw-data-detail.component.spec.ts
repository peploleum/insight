/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { RawDataDetailComponent } from 'app/entities/raw-data/raw-data-detail.component';
import { RawData } from 'app/shared/model/raw-data.model';

describe('Component Tests', () => {
    describe('RawData Management Detail Component', () => {
        let comp: RawDataDetailComponent;
        let fixture: ComponentFixture<RawDataDetailComponent>;
        const route = ({ data: of({ rawData: new RawData('123') }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [RawDataDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RawDataDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RawDataDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rawData).toEqual(jasmine.objectContaining({ id: '123' }));
            });
        });
    });
});
