import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { NetworkComponent } from 'app/network/network.component';
import { NetworkService } from './network.service';
import { Observable, of } from 'rxjs/index';

@Injectable({ providedIn: 'root' })
export class NetworkResolve implements Resolve<string> {
    constructor(private service: NetworkService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<string> {
        console.log('ROUTING');
        console.log('ROUTING');
        console.log('ROUTING');
        console.log('ROUTING');
        console.log('ROUTING');
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return of(id);
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
