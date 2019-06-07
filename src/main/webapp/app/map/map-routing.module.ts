import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { MapComponent } from './map.component';
import { QuickViewService } from '../side/quick-view.service';
import { GenericModel } from '../shared/model/generic.model';
import { Observable, of, throwError } from 'rxjs/index';
import { catchError, filter, map, switchMap } from 'rxjs/internal/operators';
import { HttpResponse } from '@angular/common/http';
import { MapService } from './map.service';
import { IGraphStructureNodeDTO } from '../shared/model/node.model';

@Injectable({ providedIn: 'root' })
export class MapResolveId implements Resolve<GenericModel[]> {
    constructor(private _qvs: QuickViewService) {}

    /**
     * => Renvoie le GenericModel de l'entité ou null
     * */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GenericModel[]> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this._qvs.find(id).pipe(
                filter((res: HttpResponse<GenericModel>) => res.ok),
                map((res: HttpResponse<GenericModel>) => [res.body])
            );
        } else {
            return of(null);
        }
    }
}

@Injectable({ providedIn: 'root' })
export class MapResolveGraph implements Resolve<IGraphStructureNodeDTO> {
    constructor(private _qvs: QuickViewService, private _ms: MapService) {}

    /**
     * => Renvoie un le schéma correspondant à l'entité
     * */
    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGraphStructureNodeDTO> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this._qvs.find(id).pipe(
                filter((res: HttpResponse<GenericModel>) => res.ok),
                map((res: HttpResponse<GenericModel>) => (res.body['externalId'] ? [res.body] : [])),
                switchMap((dto: GenericModel[]) => {
                    const order: number = this._ms.mapStates.getValue().MAX_GRAPH_DEPTH;
                    return this._qvs
                        .getGraphForEntity(dto[0]['externalId'], order)
                        .pipe(map((res: HttpResponse<IGraphStructureNodeDTO>) => res.body));
                }),
                catchError(error => throwError(error))
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
            inputData: MapResolveId
        },
        data: {
            pageTitle: 'map.title'
        }
    },
    {
        path: 'map/neighbors/:id',
        component: MapComponent,
        resolve: {
            inputSchema: MapResolveGraph
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
