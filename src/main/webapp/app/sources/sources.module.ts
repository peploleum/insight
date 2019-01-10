import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SourcesRoutingModule } from './sources-routing.module';
import { SourcesManagerComponent } from './sources-manager.component';

@NgModule({
    declarations: [SourcesManagerComponent],
    imports: [CommonModule, SourcesRoutingModule]
})
export class SourcesModule {}
