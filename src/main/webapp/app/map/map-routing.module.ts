import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { MapComponent } from './map.component';
import { QuickViewService } from '../side/quick-view.service';
import { GenericModel } from '../shared/model/generic.model';
import { Observable, of } from 'rxjs/index';
import { concatMap, filter, map, switchMap } from 'rxjs/internal/operators';
import { NetworkService } from '../network/network.service';
import { HttpResponse } from '@angular/common/http';
import { GraphDataCollection } from '../shared/model/node.model';

@Injectable({ providedIn: 'root' })
export class MapResolve implements Resolve<GenericModel> {
    constructor(private _qvs: QuickViewService, private _ns: NetworkService) {}

    /**
     * Si getNeighbors == true
     * 1) get l'entité quelques soit son type
     * 2) get les nodeDTO des neighbors de l'entité par son externalId
     * 3) get les entités des neighbors quelques soit leur type
     * => Renvoie un array de GenericModel
     * */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GenericModel[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        const getNeighbors: boolean = !!route.url.find(i => i.path === 'neighbors');
        if (getNeighbors) {
            return this._qvs.find(id).pipe(
                filter((res: HttpResponse<GenericModel>) => res.ok),
                map((res: HttpResponse<GenericModel>) => (res.body['externalId'] ? [res.body] : [])),
                concatMap((dto: GenericModel[]) =>
                    this._ns.getGraphData(dto[0]['externalId']).pipe(
                        map((data: GraphDataCollection) => data.nodes.map(node => node.mongoId)),
                        switchMap((ids: string[]) => this._qvs.findMultiple(ids).pipe(map((res: HttpResponse<GenericModel[]>) => res.body)))
                    )
                )
            );
        } else if (id) {
            return this._qvs.find(id).pipe(
                filter((res: HttpResponse<GenericModel>) => res.ok),
                map((res: HttpResponse<GenericModel>) => [res.body])
            );
        } else {
            return of(null);
        }
    }
}

const routes: Routes = [
    {
        path: 'map',
        component: MapComponent,
        data: {
            pageTitle: 'map.title'
        }
    },
    {
        path: 'map/:id',
        component: MapComponent,
        resolve: {
            inputData: MapResolve
        },
        data: {
            pageTitle: 'map.title'
        }
    },
    {
        path: 'map/neighbors/:id',
        component: MapComponent,
        resolve: {
            inputData: MapResolve
        },
        data: {
            pageTitle: 'map.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class MapRoutingModule {}
