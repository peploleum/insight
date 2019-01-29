import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { HasAnyAuthorityDirective, InsightSharedCommonModule, InsightSharedLibsModule, JhiLoginModalComponent } from './';
import { PaginationDirective } from './util/pagination.directive';
import { SideCloseComponent } from './side/side-close.component';
import { StringTruncatePipe } from './util/string-truncate.pipe';

@NgModule({
    imports: [InsightSharedLibsModule, InsightSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective, PaginationDirective, SideCloseComponent, StringTruncatePipe],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        InsightSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        PaginationDirective,
        SideCloseComponent,
        StringTruncatePipe
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class InsightSharedModule {
    static forRoot() {
        return {
            ngModule: InsightSharedModule
        };
    }
}
