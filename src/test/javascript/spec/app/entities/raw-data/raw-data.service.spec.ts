/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { RawDataService } from 'app/entities/raw-data/raw-data.service';
import { IRawData, RawData } from 'app/shared/model/raw-data.model';

describe('Service Tests', () => {
    describe('RawData Service', () => {
        let injector: TestBed;
        let service: RawDataService;
        let httpMock: HttpTestingController;
        let elemDefault: IRawData;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(RawDataService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new RawData(
                'ID',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                currentDate,
                'AAAAAAA',
                'image/png',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        rawDataCreationDate: currentDate.format(DATE_FORMAT),
                        rawDataExtractedDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a RawData', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        rawDataCreationDate: currentDate.format(DATE_FORMAT),
                        rawDataExtractedDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        rawDataCreationDate: currentDate,
                        rawDataExtractedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new RawData(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a RawData', async () => {
                const returnedFromService = Object.assign(
                    {
                        rawDataName: 'BBBBBB',
                        rawDataType: 'BBBBBB',
                        rawDataSubType: 'BBBBBB',
                        rawDataSourceName: 'BBBBBB',
                        rawDataSourceUri: 'BBBBBB',
                        rawDataSourceType: 'BBBBBB',
                        rawDataContent: 'BBBBBB',
                        rawDataCreationDate: currentDate.format(DATE_FORMAT),
                        rawDataExtractedDate: currentDate.format(DATE_FORMAT),
                        rawDataSymbol: 'BBBBBB',
                        rawDataData: 'BBBBBB',
                        rawDataCoordinates: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        rawDataCreationDate: currentDate,
                        rawDataExtractedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of RawData', async () => {
                const returnedFromService = Object.assign(
                    {
                        rawDataName: 'BBBBBB',
                        rawDataType: 'BBBBBB',
                        rawDataSubType: 'BBBBBB',
                        rawDataSourceName: 'BBBBBB',
                        rawDataSourceUri: 'BBBBBB',
                        rawDataSourceType: 'BBBBBB',
                        rawDataContent: 'BBBBBB',
                        rawDataCreationDate: currentDate.format(DATE_FORMAT),
                        rawDataExtractedDate: currentDate.format(DATE_FORMAT),
                        rawDataSymbol: 'BBBBBB',
                        rawDataData: 'BBBBBB',
                        rawDataCoordinates: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        rawDataCreationDate: currentDate,
                        rawDataExtractedDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a RawData', async () => {
                const rxPromise = service.delete('123').subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
