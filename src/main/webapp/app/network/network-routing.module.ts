import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { NetworkComponent } from 'app/network/network.component';
import { Observable, of } from 'rxjs/index';
import { QuickViewService } from '../side/quick-view.service';
import { filter, map } from 'rxjs/internal/operators';
import { GenericModel } from '../shared/model/generic.model';

@Injectable({ providedIn: 'root' })
export class NetworkResolve implements Resolve<GenericModel> {
    constructor(private _qvs: QuickViewService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GenericModel> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this._qvs.find(id).pipe(
                filter(res => res.ok),
                map(res => res.body)
            );
        } else {
            return of(null);
        }
    }
}

const routes: Routes = [
    {
        path: 'network',
        component: NetworkComponent,
        data: {
            pageTitle: 'network.title'
        }
    },
    {
        path: 'network/:id',
        component: NetworkComponent,
        resolve: {
            originNode: NetworkResolve
        },
        data: {
            pageTitle: 'network.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class NetworkRoutingModule {}
