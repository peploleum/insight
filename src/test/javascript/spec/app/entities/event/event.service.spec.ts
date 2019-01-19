/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EventService } from 'app/entities/event/event.service';
import { IEvent, Event, EventType } from 'app/shared/model/event.model';

describe('Service Tests', () => {
    describe('Event Service', () => {
        let injector: TestBed;
        let service: EventService;
        let httpMock: HttpTestingController;
        let elemDefault: IEvent;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(EventService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Event(
                'ID',
                'AAAAAAA',
                'AAAAAAA',
                EventType.POLITICAL,
                currentDate,
                'AAAAAAA',
                'image/png',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventDate: currentDate.format(DATE_TIME_FORMAT)
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

            it('should create a Event', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID',
                        eventDate: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        eventDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Event(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Event', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventName: 'BBBBBB',
                        eventDescription: 'BBBBBB',
                        eventType: 'BBBBBB',
                        eventDate: currentDate.format(DATE_TIME_FORMAT),
                        eventCoordinates: 'BBBBBB',
                        eventImage: 'BBBBBB',
                        eventSymbol: 'BBBBBB',
                        externalId: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        eventDate: currentDate
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

            it('should return a list of Event', async () => {
                const returnedFromService = Object.assign(
                    {
                        eventName: 'BBBBBB',
                        eventDescription: 'BBBBBB',
                        eventType: 'BBBBBB',
                        eventDate: currentDate.format(DATE_TIME_FORMAT),
                        eventCoordinates: 'BBBBBB',
                        eventImage: 'BBBBBB',
                        eventSymbol: 'BBBBBB',
                        externalId: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        eventDate: currentDate
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

            it('should delete a Event', async () => {
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
