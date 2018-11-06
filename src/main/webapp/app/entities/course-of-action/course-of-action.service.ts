import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseOfAction } from 'app/shared/model/course-of-action.model';

type EntityResponseType = HttpResponse<ICourseOfAction>;
type EntityArrayResponseType = HttpResponse<ICourseOfAction[]>;

@Injectable({ providedIn: 'root' })
export class CourseOfActionService {
    private resourceUrl = SERVER_API_URL + 'api/course-of-actions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/course-of-actions';

    constructor(private http: HttpClient) {}

    create(courseOfAction: ICourseOfAction): Observable<EntityResponseType> {
        return this.http.post<ICourseOfAction>(this.resourceUrl, courseOfAction, { observe: 'response' });
    }

    update(courseOfAction: ICourseOfAction): Observable<EntityResponseType> {
        return this.http.put<ICourseOfAction>(this.resourceUrl, courseOfAction, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICourseOfAction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICourseOfAction[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICourseOfAction[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
