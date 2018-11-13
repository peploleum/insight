import './vendor.ts';

import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage, LocalStorageService, SessionStorageService } from 'ngx-webstorage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { InsightSharedModule } from 'app/shared';
import { InsightCoreModule } from 'app/core';
import { InsightAppRoutingModule } from './app-routing.module';
import { InsightHomeModule } from './home/home.module';
import { InsightAccountModule } from './account/account.module';
import { InsightEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent, NavbarComponent, FooterComponent, PageRibbonComponent, ActiveMenuDirective, ErrorComponent } from './layouts';
import { MapModule } from './map/map.module';
import { NetworkModule } from './network/network.module';
import { AnalyticsModule } from './analytics/analytics.module';
import { InsightSharedAnalysisModule } from './analysis/analysis.module';
import { SourcesModule } from 'app/sources/sources.module';

@NgModule({
    imports: [
        BrowserModule,
        InsightAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        InsightSharedModule,
        InsightCoreModule,
        InsightHomeModule,
        InsightAccountModule,
        InsightEntityModule,
        MapModule,
        NetworkModule,
        AnalyticsModule,
        SourcesModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        InsightSharedAnalysisModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
            deps: [LocalStorageService, SessionStorageService]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true,
            deps: [Injector]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true,
            deps: [JhiEventManager]
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true,
            deps: [Injector]
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class InsightAppModule {}
