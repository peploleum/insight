/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { BiographicsService } from 'app/entities/biographics/biographics.service';
import { IBiographics, Biographics, Gender } from 'app/shared/model/biographics.model';

describe('Service Tests', () => {
    describe('Biographics Service', () => {
        let injector: TestBed;
        let service: BiographicsService;
        let httpMock: HttpTestingController;
        let elemDefault: IBiographics;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(BiographicsService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Biographics(
                'ID',
                'AAAAAAA',
                'AAAAAAA',
                0,
                Gender.MALE,
                'image/png',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA'
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find('123')
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Biographics', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 'ID'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Biographics(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Biographics', async () => {
                const returnedFromService = Object.assign(
                    {
                        biographicsFirstname: 'BBBBBB',
                        biographicsName: 'BBBBBB',
                        biographicsAge: 1,
                        biographicsGender: 'BBBBBB',
                        biographicsImage: 'BBBBBB',
                        biographicsCoordinates: 'BBBBBB',
                        biographicsSymbol: 'BBBBBB',
                        externalId: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Biographics', async () => {
                const returnedFromService = Object.assign(
                    {
                        biographicsFirstname: 'BBBBBB',
                        biographicsName: 'BBBBBB',
                        biographicsAge: 1,
                        biographicsGender: 'BBBBBB',
                        biographicsImage: 'BBBBBB',
                        biographicsCoordinates: 'BBBBBB',
                        biographicsSymbol: 'BBBBBB',
                        externalId: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
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

            it('should delete a Biographics', async () => {
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
