import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                ...LAYOUT_ROUTES,
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#InsightAdminModule'
                }
            ],
            { useHash: true, enableTracing: true }
        )
    ],
    exports: [RouterModule]
})
export class InsightAppRoutingModule {}
