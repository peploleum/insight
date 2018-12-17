/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InsightTestModule } from '../../../test.module';
import { RawDataComponent } from 'app/entities/raw-data/raw-data.component';
import { RawDataService } from 'app/entities/raw-data/raw-data.service';
import { RawData } from 'app/shared/model/raw-data.model';

describe('Component Tests', () => {
    describe('RawData Management Component', () => {
        let comp: RawDataComponent;
        let fixture: ComponentFixture<RawDataComponent>;
        let service: RawDataService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsightTestModule],
                declarations: [RawDataComponent],
                providers: []
            })
                .overrideTemplate(RawDataComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RawDataComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RawDataService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RawData('123')],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.rawData[0]).toEqual(jasmine.objectContaining({ id: '123' }));
        });
    });
});
