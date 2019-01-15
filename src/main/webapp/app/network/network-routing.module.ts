import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { NetworkComponent } from 'app/network/network.component';
import { NetworkService } from './network.service';
import { Observable, of } from 'rxjs/index';

@Injectable({ providedIn: 'root' })
export class NetworkResolve implements Resolve<any> {
    constructor(private service: NetworkService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.getGraphData(id);
        }
        return of({
            nodes: [],
            edges: []
        });
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
            idOrigin: NetworkResolve
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
