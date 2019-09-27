import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { CardComponent } from './card.component';
import { Observable } from 'rxjs';
import { IBiographics } from 'app/shared/model/biographics.model';
import { BiographicsService } from 'app/entities/biographics';
import { HttpResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class BiographicsCardResolve implements Resolve<IBiographics> {
    constructor(private service: BiographicsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBiographics> {
        const id = route.params.id ? route.params.id : null;
        if (id) {
            return this.service.find(id).pipe(
                map((res: HttpResponse<IBiographics>) => {
                    return res.body as IBiographics;
                })
            );
        }
    }
}

const routes: Routes = [
    {
        path: 'card/analytics/:id',
        component: CardComponent,
        resolve: {
            biographics: BiographicsCardResolve
        },
        data: {
            pageTitle: 'map.title'
        }
    },
    {
        path: 'card',
        component: CardComponent,
        data: {
            pageTitle: 'map.title'
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CardRoutingModule {}
