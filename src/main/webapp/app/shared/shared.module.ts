import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { InsightSharedLibsModule, InsightSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';
import { PaginationDirective } from './util/pagination.directive';

@NgModule({
    imports: [InsightSharedLibsModule, InsightSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, PaginationDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [InsightSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, PaginationDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightSharedModule {
    static forRoot() {
        return {
            ngModule: InsightSharedModule
        };
    }
}
