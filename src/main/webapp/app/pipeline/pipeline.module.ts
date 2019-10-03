import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PipelineComponent } from './pipeline.component';
import { pipelineRoute } from 'app/pipeline/pipeline.route';
import { InsightSharedModule } from 'app/shared';
import { RouterModule } from '@angular/router';
import { ProcessTableComponent } from './process-table/process-table.component';
import { LoadTableComponent } from './load-table/load-table.component';
import { FileDropModule } from 'ngx-file-drop';

const ENTITY_STATES = [...pipelineRoute];

@NgModule({
    declarations: [PipelineComponent, ProcessTableComponent, LoadTableComponent],
    imports: [CommonModule, InsightSharedModule, FileDropModule, RouterModule.forChild(ENTITY_STATES)]
})
export class PipelineModule {}
