import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access-service';
import { DictionaryComponent } from 'app/dictionary/dictionary.component';

export const dictionaryRoute: Routes = [
    {
        path: 'dictionary',
        component: DictionaryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'insightApp.dictionary.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
