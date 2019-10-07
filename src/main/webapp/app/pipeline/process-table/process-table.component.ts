import { Component, OnInit } from '@angular/core';
import { PipelineService } from 'app/pipeline/pipeline.service';
import { Observable } from 'rxjs';
import { IProcessedFormFile } from 'app/shared/model/pipeline.model';

@Component({
    selector: 'ins-process-table',
    templateUrl: './process-table.component.html',
    styleUrls: ['process-table.component.scss']
})
export class ProcessTableComponent implements OnInit {
    constructor(private _ps: PipelineService) {}

    ngOnInit() {}

    getProcessedFiles(): Observable<IProcessedFormFile[]> {
        return this._ps.processedFiles;
    }
}
