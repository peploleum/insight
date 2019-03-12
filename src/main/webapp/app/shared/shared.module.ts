import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { HasAnyAuthorityDirective, InsightSharedCommonModule, InsightSharedLibsModule, JhiLoginModalComponent } from './';
import { PaginationDirective } from './util/pagination.directive';
import { StringTruncatePipe } from './util/string-truncate.pipe';
import { ListFilterByStringPipe, ListFilterPipe } from './util/list-filter.pipe';
import { TextExpanderDirective } from './util/text-expander.directive';
import { RawdataHyperlinkComponent } from './hyperlink/rawdata-hyperlink.component';
import { HyperlinkDirective } from './hyperlink/hyperlink.directive';
import { SafeHtmlPipe } from './util/safe-html.pipe';
import { HyperlinkPopoverComponent } from './hyperlink/hyperlink-popover.component';
import { NetworkRoutingModule } from '../network/network-routing.module';
import { ModalWindowComponent } from './modal-window/modal-window.component';

@NgModule({
    imports: [InsightSharedLibsModule, InsightSharedCommonModule, NetworkRoutingModule],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        PaginationDirective,
        StringTruncatePipe,
        ListFilterPipe,
        ListFilterByStringPipe,
        TextExpanderDirective,
        RawdataHyperlinkComponent,
        HyperlinkDirective,
        SafeHtmlPipe,
        HyperlinkPopoverComponent,
        ModalWindowComponent
    ],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent, HyperlinkPopoverComponent],
    exports: [
        InsightSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        PaginationDirective,
        StringTruncatePipe,
        ListFilterPipe,
        ListFilterByStringPipe,
        TextExpanderDirective,
        RawdataHyperlinkComponent,
        HyperlinkDirective,
        SafeHtmlPipe,
        HyperlinkPopoverComponent,
        ModalWindowComponent
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
