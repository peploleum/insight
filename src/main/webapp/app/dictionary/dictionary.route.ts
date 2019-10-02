import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { DictionaryComponent } from 'app/dictionary/dictionary.component';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { IDictionary } from 'app/shared/model/analytics.model';
import { DictionaryService } from 'app/dictionary/dictionary.service';

@Injectable({ providedIn: 'root' })
export class DictionaryResolve implements Resolve<IDictionary[]> {
    constructor(private service: DictionaryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDictionary[]> {
        return this.service.getAll().pipe(
            map((res: HttpResponse<IDictionary[]>) => {
                return res.body;
            })
        );
    }
}

export const dictionaryRoute: Routes = [
    {
        path: 'dictionary',
        component: DictionaryComponent,
        resolve: {
            dictionaries: DictionaryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.dictionary.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
