/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InsightTestModule } from '../../../test.module';
import { ObservedDataDetailComponent } from 'app/entities/observed-data/observed-data-detail.component';
import { ObservedData } from 'app/shared/model/observed-data.model';

describe('Component Tests', () => {
    describe('ObservedData Management Detail Component', () => {
        let comp: ObservedDataDetailComponent;
        let fixture: ComponentFixture<ObservedDataDetailComponent>;
        const route = ({ data: of({ observedData: new ObservedData(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [ObservedDataDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ObservedDataDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ObservedDataDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.observedData).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
