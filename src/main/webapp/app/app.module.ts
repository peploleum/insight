import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { NgJhipsterModule } from 'ng-jhipster';

import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { InsightSharedModule } from 'app/shared';
import { InsightCoreModule } from 'app/core';
import { InsightAppRoutingModule } from './app-routing.module';
import { InsightHomeModule } from './home/home.module';
import { InsightAccountModule } from './account/account.module';
import { InsightEntityModule } from './entities/entity.module';
import * as moment from 'moment';
import localeFr from '@angular/common/locales/fr';

// jhipster-needle-angular-add-module-import JHipster will add new module here
import { ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent } from './layouts';
import { DashboardModule } from 'app/dashboard/dashboard.module';
import { MapModule } from 'app/map/map.module';
import { SourcesModule } from 'app/sources/sources.module';
import { NetworkModule } from 'app/network/network.module';
import { SideModule } from './side/side.module';
import { registerLocaleData } from '@angular/common';

/**
 * Si Locale ne fonctionne pas en AOT:
 * @NgModule({ { provide: LOCALE_ID, useValue: 'de' } })
 * And then you also need to add the npm dependency to your project.config
 * { name: '@angular/common/locales/fr', path: '${this.NPM_BASE}@angular/common/locales/fr.js' }
 * */
registerLocaleData(localeFr, 'fr-FR');
@NgModule({
    imports: [
        BrowserModule,
        InsightAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-' }),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            alertTimeout: 5000,
            i18nEnabled: true,
            defaultI18nLang: 'en'
        }),
        InsightSharedModule.forRoot(),
        InsightCoreModule,
        InsightHomeModule,
        InsightAccountModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
        InsightEntityModule,
        DashboardModule,
        MapModule,
        SideModule,
        SourcesModule,
        NetworkModule
    ],
    declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthExpiredInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorHandlerInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: NotificationInterceptor,
            multi: true
        }
    ],
    bootstrap: [JhiMainComponent]
})
export class InsightAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = { year: moment().year() - 100, month: 1, day: 1 };
    }
}
